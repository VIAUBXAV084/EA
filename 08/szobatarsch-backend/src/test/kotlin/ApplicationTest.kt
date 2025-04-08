package hu.bme.kszk

import hu.bme.kszk.db.DatabaseFactory
import hu.bme.kszk.db.TestDatabases
import hu.bme.kszk.db.configureDatabases
import hu.bme.kszk.db.users.UsersEntity
import hu.bme.kszk.dto.users.UserResponse
import hu.bme.kszk.dto.users.toUserResponse
import hu.bme.kszk.plugins.configureHTTP
import hu.bme.kszk.plugins.configureRouting
import hu.bme.kszk.plugins.configureSecurity
import hu.bme.kszk.plugins.configureSerialization
import hu.bme.kszk.routes.users.userRoutes
import hu.bme.kszk.service.MainService
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import kotlinx.coroutines.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.test.*

class ApplicationTest {

    private fun ApplicationTestBuilder.testConfig() {
        application {
            configureSerialization()
            install(ContentNegotiation) {
                json(Json { prettyPrint = false })
            }
            configureSecurity(MainService.usersService, MainService.roomsService)
            configureRouting(MainService)
        }
    }

    @BeforeTest
    fun setup() = testApplication {
        application {
            configureDatabases(TestDatabases)
        }
    }

    @Test
    fun testRoot() = testApplication {
        testConfig()

        runBlocking {
            val response = client.get("/")
            assertEquals(HttpStatusCode.OK, response.status)
            assertEquals(
                """
                <!DOCTYPE html>
                <html>
                  <body>
                    <p><a href="/login">Login with AuthSCH</a></p>
                  </body>
                </html>
                
                """
            .trimIndent(), response.bodyAsText())
        }
    }

    @Test
    fun testUsers() = testApplication {
        testConfig()

        runBlocking {
            val users: MutableList<UserResponse> = mutableListOf()
            transaction {
                users.addAll(UsersEntity.all().map { it.toUserResponse() })
            }

            client.get("api/v1/users").let {
                assertEquals(HttpStatusCode.OK, it.status)
                val response = it.bodyAsText()
                assertEquals(Json.encodeToString(users), response)
            }

            client.get("api/v1/users/id/${users[0].userUuid}").let {
                assertEquals(HttpStatusCode.OK, it.status)
                val response = it.bodyAsText()
                assertEquals(Json.encodeToString(users[0]), response)
            }
        }
    }




}
