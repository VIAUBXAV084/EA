package hu.bme.kszk.plugins

import hu.bme.kszk.db.users.UsersEntity
import hu.bme.kszk.dto.users.UserRequest
import hu.bme.kszk.service.rooms.regular.RoomsServiceInterface
import hu.bme.kszk.service.users.users.UsersServiceInterface
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.apache.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.http.headers
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.html.*
import io.ktor.server.plugins.csrf.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import kotlinx.html.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.transactions.transaction


val applicationHttpClient = HttpClient(CIO) {
    install(ContentNegotiation) {
        json()
    }
}

fun Application.configureSecurity(usersService: UsersServiceInterface, roomsService: RoomsServiceInterface) {

    install(Sessions) {
        //header<UserSession>("user_session", SessionStorageMemory()) //POSTMAN teszteléshez
        
        //Böngészős használathoz
        cookie<UserSessionCookie>("user_session_cookie", SessionStorageMemory()) {
            cookie.path = "/"
            cookie.maxAgeInSeconds = 60*60
        }
    }
    val redirects = mutableMapOf<String, String>()

    install(Authentication) {
        oauth("auth-sch") {
            // Configure oauth authentication
            urlProvider = { System.getenv("REDIRECT_URL") }
            // skipWhen { call -> call.sessions.get<UserSession>() != null } // POSTMAN
            skipWhen { call -> call.sessions.get<UserSessionCookie>() != null }  // Böngésző

            providerLookup = {
                OAuthServerSettings.OAuth2ServerSettings(
                    name = "auth-sch",
                    authorizeUrl = "https://auth.sch.bme.hu/site/login",
                    accessTokenUrl = "https://auth.sch.bme.hu/oauth2/token",
                    requestMethod = HttpMethod.Post,
                    clientId = System.getenv("AUTH_SCH_CLIENT_ID"),
                    clientSecret = System.getenv("AUTH_SCH_CLIENT_SECRET"),
                    defaultScopes = listOf("openid", "profile", "email", "directory.sch.bme.hu:sAMAccountName"),
                    extraAuthParameters = listOf("access_type" to "offline"),
                    onStateCreated = { call, state ->
                        //saves new state with redirect url value
                        call.request.queryParameters["redirectUrl"]?.let {
                            redirects[state] = it
                        }
                    }
                )
            }

            client = applicationHttpClient
        }
    }
    routing {
        authenticate("auth-sch") {
            get("/login") {
                // Az oauth plugin automatikusan kezeli az átirányítást
            }

            get("/callback") {

                println(call.request)
                val principal: OAuthAccessTokenResponse.OAuth2? = call.principal()


                println(principal.toString())
                if (principal == null) {
                    println("Authentication failed: principal is null")
                    call.respond(HttpStatusCode.Unauthorized, "Authentication failed")
                    return@get
                }

                val response = applicationHttpClient.get("https://auth.sch.bme.hu/oidc/userinfo") {
                    headers {
                        append(HttpHeaders.Authorization, "Bearer ${principal.accessToken}") // 🔹 Token hozzáadása
                        append(HttpHeaders.Accept, ContentType.Application.Json.toString()) // 🔹 JSON válasz kérés
                    }
                }
                val userInfo: UserInfo2 = response.body()

                var user:  UsersEntity? = null
                transaction {
                    user = usersService.getUserByAuthSchId(userInfo.sub)
                    if (user == null) {
                        user = usersService.createUser(UserRequest(
                            schacc = userInfo.sAMAccountName,
                            name = userInfo.name,
                            email = userInfo.email,
                            authSchId = userInfo.sub,
                        ),
                            roomsService = roomsService)
                    }
                }

                //UserId = "sub" ezzel meg db-ben ellenőrzés
                //userId a saját User entity uuidja

                //call.sessions.set(UserSession(user!!.id.value.toString())) //POSTMAN
                call.sessions.set(UserSessionCookie(user!!.id.value.toString())) //Böngészős használathoz

                call.respondRedirect("/home")
            }
        }

        get("/") {
            call.respondHtml {
                body {
                    p {
                        a("/login") { +"Login with AuthSCH" }
                    }
                }
            }
        }

        get("/home") {
            //WEB
            val userSession: UserSessionCookie? = call.sessions.get<UserSessionCookie>()
            if (userSession == null) {
                call.respondRedirect("/login")
                return@get
            }


            //POSTMAN
            //val userSession: UserSession? = call.sessions.get<UserSession>()
            //if (userSession == null) {
            //    call.respondRedirect("/login")
            //    return@get
            //}

            try {
                //val userInfo: UserInfo = getPersonalGreeting(applicationHttpClient, userSession)
                //call.respondText("Hello, ${userInfo.name}! Welcome home!")
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, "Failed to fetch user info")
            }
            call.respond(
                HttpStatusCode.OK,
                "Hello World")

        }

    }

    //install(CSRF) {
      // tests Origin is an expected value
      //allowOrigin("http://localhost:6011")

      // tests Origin matches Host header
      //originMatchesHost()
    
      // custom header checks
      //checkHeader("X-CSRF-Token")
    //}
}

@Serializable
data class UserSession(val userId: String)
// "sub" = userId

@Serializable
data class UserSessionCookie(val userId: String)

@Serializable
data class UserInfo2(
    val name: String,
    @SerialName("family_name") val familyName: String,
    @SerialName("given_name") val givenName: String,
    val birthdate: String,
    val updated_at: Long,
    val email: String,
    val email_verified: Boolean,
    @SerialName("directory.sch.bme.hu:sAMAccountName") val sAMAccountName: String,
    @SerialName("meta.directory.sch.bme.hu:updated_at") val directoryUpdatedAt: Long,
    val sub: String
)


@Serializable
data class UserInfo(
    val name: String,
    @SerialName("family_name") val familyName: String,
    @SerialName("given_name") val givenName: String,
    val birthdate: String? = null,
    val address: Address? = null,
    @SerialName("bme.hu:niifPersonOrgID") val personOrgID: String? = null,
    @SerialName("bme.hu:eduPersonPrincipalName") val eduPersonPrincipalName: String? = null,
    @SerialName("bme.hu:niifEduPersonAttendedCourse/v1") val attendedCourse: String? = null,
    @SerialName("meta.bme.hu:unitScope") val unitScope: String? = null,
    @SerialName("meta.bme.hu:updated_at") val metaUpdatedAt: Long? = null,
    val email: String,
    val email_verified: Boolean,
    @SerialName("directory.sch.bme.hu:groups/v1") val groups: List<String>? = null,
    @SerialName("directory.sch.bme.hu:sAMAccountName") val sAMAccountName: String? = null,
    @SerialName("meta.directory.sch.bme.hu:updated_at") val directoryUpdatedAt: Long? = null,
    @SerialName("pek.sch.bme.hu:entrants/v1") val entrants: List<Entrant>? = null,
    @SerialName("pek.sch.bme.hu:executiveAt/v1") val executiveAt: List<Executive>? = null,
    @SerialName("pek.sch.bme.hu:activeMemberships/v1") val activeMemberships: List<Membership>? = null,
    @SerialName("pek.sch.bme.hu:alumniMemberships/v1") val alumniMemberships: List<AlumniMembership>? = null,
    @SerialName("meta.pek.sch.bme.hu:updated_at") val pekMetaUpdatedAt: Long? = null,
    val phone_number: String? = null,
    val phone_number_verified: Boolean = false,
    val roles: List<String>? = null,
    val sub: String = "",
    val updated_at: Long? = null
)

@Serializable
data class Address(
    val formatted: String
)

@Serializable
data class Entrant(
    val groupId: Int,
    val groupName: String,
    val entrantType: String
)

@Serializable
data class Executive(
    val id: Int,
    val name: String
)

@Serializable
data class Membership(
    val id: Int,
    val name: String,
    val title: List<String>
)

@Serializable
data class AlumniMembership(
    val id: Int,
    val name: String,
    val start: String,
    val end: String
)

