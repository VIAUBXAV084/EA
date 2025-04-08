package hu.bme.kszk.service.users.users


import hu.bme.kszk.db.users.UsersEntity
import hu.bme.kszk.dto.users.UserRequest
import hu.bme.kszk.service.rooms.regular.RoomsServiceInterface
import java.util.*

interface UsersServiceInterface {
    fun getAllUsers() : List<UsersEntity>
    fun getUserById(uuid: String) : UsersEntity?
    fun getUserById(uuid: UUID) : UsersEntity?
    fun getUserByAuthSchId(authSchId: String): UsersEntity?
    fun getUserBySchacc(schacc: String) : UsersEntity?
    fun getUsersByRoom(uuid: String) : List<UsersEntity>
    fun getUsersByBroState(uuid: String) : List<UsersEntity>
    fun createUser(createUserRequest: UserRequest, roomsService: RoomsServiceInterface) : UsersEntity
    fun updateUser(updateUserRequest: UserRequest, roomsService: RoomsServiceInterface) : UsersEntity?
    fun deleteUser(uuid: String) : Unit
}
