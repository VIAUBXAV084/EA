package hu.bme.kszk.service.api

import hu.bme.kszk.screen.Screen
import hu.bme.kszk.service.api.dto.rooms.RoomRequest
import hu.bme.kszk.service.api.dto.rooms.RoomResponse
import hu.bme.kszk.service.api.dto.usecases.BroingResponse
import hu.bme.kszk.service.api.dto.usecases.DiscoverResponse
import hu.bme.kszk.service.api.dto.usecases.OwnRoomResponse
import hu.bme.kszk.service.api.dto.users.UserResponse
import hu.bme.kszk.util.NetworkError
import hu.bme.kszk.util.Result
import hu.bme.kszk.util.onError
import hu.bme.kszk.util.onSuccess
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json

expect fun getHttpClientEngine(): HttpClientEngine

fun createHttpClient(engine: HttpClientEngine = getHttpClientEngine()): HttpClient {
    return HttpClient(engine){
        install(Logging){
            level = LogLevel.ALL
        }
        install(ContentNegotiation){
            json(
                json = Json {
                    ignoreUnknownKeys = true
                    prettyPrint = true
                }
            )
        }
        //install(Auth){
        //    bearer {
        //        loadTokens {  }
        //        refreshTokens {   }
        //    }
        //}
        defaultRequest {
            //TODO "Androidon használd a saját IP-d írd át az androidMian/res/xml értékét, Ne loaclhost legyen, más platformon a loaclhost is megfelel"
            url("http://localhost:6011")  // TODO Set the base URL
            //url("https://szobatarsch.kszk.bme.hu")
            //authToken?.let { token ->
            //    header(HttpHeaders.Authorization, "Bearer $token")  // Add the Bearer token if it's not null
            //}
        }
    }
}

interface ApiServiceInterface{
    val myId: String  //TODO remove later
    suspend fun getAllUser(): Result<List<UserResponse>, NetworkError>
    suspend fun getDiscoverScreenData(userId: String = myId): Result<DiscoverResponse, NetworkError>

    suspend fun getOwnRoomScreenData(userId: String = myId): Result<OwnRoomResponse, NetworkError>

    suspend fun getUserRoom(userId: String = myId): Result<RoomResponse, NetworkError>

    suspend fun getBroingData(userId: String = myId): Result<BroingResponse, NetworkError>

    //suspend fun roomInviteRequest() TODO
    suspend fun updateRoom(room: RoomRequest): Result<RoomResponse, NetworkError>
}

class ApiServiceImpl(engine: HttpClientEngine): ApiServiceInterface {
    private val client = createHttpClient(engine)

    override val myId: String
        get() = "e971a6d6-5924-48f5-b488-c6801a4689d3"//"8c262f7e-b17f-424f-8a88-673d8966eaa7"
            //"e971a6d6-5924-48f5-b488-c6801a4689d3"

    override suspend fun getAllUser(): Result<List<UserResponse>, NetworkError> {
        val response = try {
            client.get("/api/v1/users")
        } catch (e: UnresolvedAddressException) {
            return Result.Error(NetworkError.NO_INTERNET)
        } catch (e: SerializationException) {
            return Result.Error(NetworkError.SERIALIZATION)
        } catch (e: ConnectTimeoutException) {
            return Result.Error(NetworkError.CONNECT_TIMEOUT)
        } catch (e: Exception) {
            return Result.Error(NetworkError.UNKNOWN)
        }

        return when(response.status.value){
            in 200..299 -> {
                val data = response.body<List<UserResponse>>()
                Result.Success(data)
            }
            401 -> Result.Error(NetworkError.UNAUTHORIZED)
            408 -> Result.Error(NetworkError.REQUEST_TIMEOUT)
            409 -> Result.Error(NetworkError.CONFLICT)
            413 -> Result.Error(NetworkError.PAYLOAD_TOO_LARGE)
            in 500..509 -> Result.Error(NetworkError.SERVER_ERROR)
            else ->  Result.Error(NetworkError.UNKNOWN)
        }
    }

    override suspend fun getDiscoverScreenData(userId: String): Result<DiscoverResponse, NetworkError> {
        val response = try {
            client.get("api/v1/discover/${userId}")
        } catch (e: UnresolvedAddressException) {
            return Result.Error(NetworkError.NO_INTERNET)
        } catch (e: SerializationException) {
            return Result.Error(NetworkError.SERIALIZATION)
        } catch (e: ConnectTimeoutException) {
            return Result.Error(NetworkError.CONNECT_TIMEOUT)
        } catch (e: Exception) {
            return Result.Error(NetworkError.UNKNOWN)
        }

        return when(response.status.value){
            in 200..299 -> {
                val data = response.body<DiscoverResponse>()
                Result.Success(data)
            }
            401 -> Result.Error(NetworkError.UNAUTHORIZED)
            408 -> Result.Error(NetworkError.REQUEST_TIMEOUT)
            409 -> Result.Error(NetworkError.CONFLICT)
            413 -> Result.Error(NetworkError.PAYLOAD_TOO_LARGE)
            in 500..509 -> Result.Error(NetworkError.SERVER_ERROR)
            else ->  Result.Error(NetworkError.UNKNOWN)
        }
    }

    override suspend fun getOwnRoomScreenData(userId: String): Result<OwnRoomResponse, NetworkError> {
        val response = try {
            client.get("api/v1/ownroom/${userId}")
        } catch (e: UnresolvedAddressException) {
            return Result.Error(NetworkError.NO_INTERNET)
        } catch (e: SerializationException) {
            return Result.Error(NetworkError.SERIALIZATION)
        } catch (e: ConnectTimeoutException) {
            return Result.Error(NetworkError.CONNECT_TIMEOUT)
        } catch (e: Exception) {
            return Result.Error(NetworkError.UNKNOWN)
        }

        return when(response.status.value){
            in 200..299 -> {
                val data = response.body<OwnRoomResponse>()
                Result.Success(data)
            }
            401 -> Result.Error(NetworkError.UNAUTHORIZED)
            408 -> Result.Error(NetworkError.REQUEST_TIMEOUT)
            409 -> Result.Error(NetworkError.CONFLICT)
            413 -> Result.Error(NetworkError.PAYLOAD_TOO_LARGE)
            in 500..509 -> Result.Error(NetworkError.SERVER_ERROR)
            else ->  Result.Error(NetworkError.UNKNOWN)
        }
    }

    override suspend fun getUserRoom(userId: String): Result<RoomResponse, NetworkError> {
        val response = try {
            client.get("api/v1/rooms/user/${userId}")
        } catch (e: UnresolvedAddressException) {
            return Result.Error(NetworkError.NO_INTERNET)
        } catch (e: SerializationException) {
            return Result.Error(NetworkError.SERIALIZATION)
        } catch (e: ConnectTimeoutException) {
            return Result.Error(NetworkError.CONNECT_TIMEOUT)
        } catch (e: Exception) {
            return Result.Error(NetworkError.UNKNOWN)
        }

        return when(response.status.value){
            in 200..299 -> {
                val data = response.body<RoomResponse>()
                Result.Success(data)
            }
            401 -> Result.Error(NetworkError.UNAUTHORIZED)
            408 -> Result.Error(NetworkError.REQUEST_TIMEOUT)
            409 -> Result.Error(NetworkError.CONFLICT)
            413 -> Result.Error(NetworkError.PAYLOAD_TOO_LARGE)
            in 500..509 -> Result.Error(NetworkError.SERVER_ERROR)
            else ->  Result.Error(NetworkError.UNKNOWN)
        }
    }

    override suspend fun getBroingData(userId: String): Result<BroingResponse, NetworkError> {
        val response = try {
            client.get("api/v1/broing/${userId}")
        } catch (e: UnresolvedAddressException) {
        return Result.Error(NetworkError.NO_INTERNET)
        } catch (e: SerializationException) {
            return Result.Error(NetworkError.SERIALIZATION)
        } catch (e: ConnectTimeoutException) {
            return Result.Error(NetworkError.CONNECT_TIMEOUT)
        } catch (e: Exception) {
            return Result.Error(NetworkError.UNKNOWN)
        }

        return when(response.status.value){
            in 200..299 -> {
                val data = response.body<BroingResponse>()
                Result.Success(data)
            }
            401 -> Result.Error(NetworkError.UNAUTHORIZED)
            408 -> Result.Error(NetworkError.REQUEST_TIMEOUT)
            409 -> Result.Error(NetworkError.CONFLICT)
            413 -> Result.Error(NetworkError.PAYLOAD_TOO_LARGE)
            in 500..509 -> Result.Error(NetworkError.SERVER_ERROR)
            else ->  Result.Error(NetworkError.UNKNOWN)
        }
    }

    //suspend fun roomInviteRequest() TODO

    override suspend fun updateRoom(room: RoomRequest): Result<RoomResponse, NetworkError> {
        val response = try {
            client.put("api/v1/rooms") {
                contentType(ContentType.Application.Json)
                setBody(room)
            }
        } catch (e: UnresolvedAddressException) {
            return Result.Error(NetworkError.NO_INTERNET)
        } catch (e: SerializationException) {
            return Result.Error(NetworkError.SERIALIZATION)
        } catch (e: ConnectTimeoutException) {
            return Result.Error(NetworkError.CONNECT_TIMEOUT)
        } catch (e: Exception) {
            return Result.Error(NetworkError.UNKNOWN)
        }

        return when(response.status.value){
            in 200..299 -> {
                val data = response.body<RoomResponse>()
                Result.Success(data)
            }
            401 -> Result.Error(NetworkError.UNAUTHORIZED)
            408 -> Result.Error(NetworkError.REQUEST_TIMEOUT)
            409 -> Result.Error(NetworkError.CONFLICT)
            413 -> Result.Error(NetworkError.PAYLOAD_TOO_LARGE)
            in 500..509 -> Result.Error(NetworkError.SERVER_ERROR)
            else ->  Result.Error(NetworkError.UNKNOWN)
        }
    }
}

