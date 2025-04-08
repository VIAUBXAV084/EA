package hu.bme.kszk.service.usecases


import hu.bme.kszk.db.rooms.RoomsEntity
import hu.bme.kszk.db.users.UsersEntity
import hu.bme.kszk.dto.users.BroState
import hu.bme.kszk.service.rooms.hidden.HiddenRoomServiceInterface
import hu.bme.kszk.service.rooms.regular.RoomsServiceInterface
import hu.bme.kszk.service.users.bros.BroServiceInterface
import hu.bme.kszk.service.users.users.UsersServiceInterface
import hu.bme.kszk.szobatarsch.enums.BroStates
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class UsecasesService: UsecasesServiceInterface {
    override fun getMyBros(user: String, brosService: BroServiceInterface): List<UsersEntity> = transaction {
        val myBros: MutableList<UsersEntity> = mutableListOf()
        myBros.addAll(brosService.getBroByTargetUserId(user, BroState(BroStates.ACCEPTED.toString()))
            .map { it.inviterUser }.toMutableList())
        myBros.addAll(brosService.getBroByInviterUserId(user, BroState(BroStates.ACCEPTED.toString())).map { it.targetUser })
        myBros
    }

    override fun getUserRequests(user: String, brosService: BroServiceInterface): List<UsersEntity> = transaction {
        val requests: MutableList<UsersEntity> = mutableListOf()
        requests.addAll(
            brosService.getBroByTargetUserId(user, BroState(BroStates.PENDING.toString())).map { it.inviterUser })
        requests
    }

    override fun getOthers(user: String, brosService: BroServiceInterface, usersService: UsersServiceInterface): List<UsersEntity> = transaction {
        val others: MutableList<UsersEntity> = mutableListOf()
        others.addAll(usersService.getAllUsers().toMutableList())
        others.removeAll(getMyBros(user, brosService))
        others.removeAll(getUserRequests(user, brosService))
        others.removeAll { it.id.value == UUID.fromString(user) }
        others
    }

    override fun getOtherRooms(user: String, usersService: UsersServiceInterface, roomsService: RoomsServiceInterface, hiddenRoomService: HiddenRoomServiceInterface): List<RoomsEntity> = transaction {
        val userRoom = usersService.getUserById(user)?.room
        val hiddenRoomIdList = hiddenRoomService.getHiddenRoomsByUserId(user).map { it.hiddenRoom.id.value }
        roomsService.getAllRooms().filterNot { it.id.value == userRoom?.id?.value }.filterNot { hiddenRoomIdList.contains(it.id.value) }
    }

    override fun getRoomMembers(user: String, usersService: UsersServiceInterface): List<UsersEntity> = transaction {
        val roomMates: MutableList<UsersEntity> = mutableListOf()
        usersService.getUserById(user)?.let { userEntity -> userEntity.room?.let { usersService.getUsersByRoom(userEntity.room?.id?.value.toString()) }}.let{ roomMates.addAll(it?:emptyList()) }
        roomMates
    }

    override fun getRoomMemberCount(usersService: UsersServiceInterface): Map<String, Int> = transaction {
        val roomCapacity = mutableMapOf<String, Int>()
        usersService.getAllUsers().forEach { user ->
            user.room?.let {
                val capacity: Int = roomCapacity[user.room?.id?.value.toString()] ?: 0
                roomCapacity.putIfAbsent(user.room?.id?.value.toString(), capacity+1)
            }
        }
        roomCapacity
    }

    override fun getCompatibleRooms(
        user: String,
        roomsService: RoomsServiceInterface,
        usersService: UsersServiceInterface,
        hiddenRoomService: HiddenRoomServiceInterface
    ): List<RoomsEntity> = transaction {
        val compatibleRooms: MutableList<RoomsEntity> = mutableListOf()
        val roomMemberCount = getRoomMemberCount(usersService)
        val myRoomMemberCount = roomMemberCount[usersService.getUserById(user)?.room?.id?.value.toString()]?:0
        val myRoomId = usersService.getUserById(user)?.room?.id?.value
        roomsService.getAllRooms().filter { (it.capacity >= (myRoomMemberCount + (roomMemberCount[it.id.value.toString()] ?:0))) and (it.id.value != myRoomId) }.forEach { room ->
            compatibleRooms.add(room)
        }
        compatibleRooms.filterNot { hiddenRoomService.getHiddenRoomsByUserId(user).map { it.hiderUser.id.value.toString() }.contains(user) }
    }


}