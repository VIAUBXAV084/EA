package hu.bme.kszk.db

import hu.bme.kszk.db.rooms.*
import hu.bme.kszk.db.users.*
import hu.bme.kszk.enums.Gender
import hu.bme.kszk.enums.Major
import hu.bme.kszk.szobatarsch.enums.*
import org.h2.engine.User
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

interface DatabaseInterface {
    val db: Database

    //TODO write init script for table creating and default data
    fun createSampleData(){
        var user1: UsersEntity? = null
        var user2: UsersEntity? = null
        var user3: UsersEntity? = null
        var user4: UsersEntity? = null
        var room1: RoomsEntity? = null
        var room2: RoomsEntity? = null
        var room3: RoomsEntity? = null
        var roomMergeRequestsEntity: RoomMergeRequestsEntity? = null


        transaction {
            if(UsersEntity.all().empty()) {
                user1 = UsersEntity.new {
                    schacc = "tester"
                    name = "Teszt Elek"
                    email = "test@test.com"
                    nickname = "Teszter"
                    profileDescription = "Lorem ipsum dolor sit amet"
                    gender = Gender.FEMALE
                    major = Major.BProf
                    wantsCoedRoom = true
                    isMentor = false
                    isSearchingRoom = false
                    authSchId = UUID.randomUUID().toString()
                }
            }
        }
        transaction {
            if (UsersEntity.all().count() == 1L) {
                user2 = UsersEntity.new {
                    schacc = "randomuser"
                    name = "Random Jozsi"
                    email = "random@random.com"
                    nickname = "Rando"
                    profileDescription = "Random profile description"
                    gender = Gender.MALE
                    major = Major.MI
                    wantsCoedRoom = false
                    isMentor = true
                    isSearchingRoom = false
                    authSchId = UUID.randomUUID().toString()
                }

            }
        }

        transaction {
            if (UsersEntity.all().count() == 2L) {
                user3 = UsersEntity.new {
                    schacc = "bro"
                    name = "My Bro"
                    email = "bro@random.com"
                    nickname = "Bro"
                    profileDescription = "Bro profile description"
                    gender = Gender.MALE
                    major = Major.MI
                    wantsCoedRoom = false
                    isMentor = true
                    isSearchingRoom = false
                    authSchId = UUID.randomUUID().toString()
                }

                BrosEntity.new {
                    inviterUser = user1?: return@new
                    targetUser = user3?: return@new
                    state = BroStates.ACCEPTED

                }
            }
        }

        transaction {
            if (UsersEntity.all().count() == 3L) {
                user4 = UsersEntity.new {
                    schacc = "Asd"
                    name = "Asd"
                    email = "asd@asd.com"
                    nickname = "Asd"
                    profileDescription = "Asd profile description"
                    gender = Gender.MALE
                    major = Major.MI
                    wantsCoedRoom = false
                    isMentor = true
                    isSearchingRoom = false
                    authSchId = UUID.randomUUID().toString()
                }
            }
        }

        transaction {
            if(RoomsEntity.all().empty()) {
                room1 = RoomsEntity.new {
                    roomNumber = 105
                    roomDescription = "Egy vidám szoba, sok fénnyel."
                    isCoedRoom = true
                    hasLovers = true
                    isQuiet = false
                    isSocial = true
                    orientation = RoomOrientation.SOUTH_WEST
                    nickname = "Százöt"
                    capacity = 4
                }
                user1!!.room = room1
                user4!!.room = room1
            }
        }
        transaction {
            if(RoomsEntity.all().count() == 1L) {
                room2 = RoomsEntity.new {
                    roomNumber = 204
                    roomDescription = "Csendes, nyugodt tanulószoba."
                    isCoedRoom = false
                    hasLovers = false
                    isQuiet = true
                    isSocial = false
                    orientation = RoomOrientation.NORTH_EAST
                    nickname = "Kettő-négy"
                    capacity = 4
                }
                user2!!.room = room2
            }
        }

        transaction {
            if(RoomsEntity.all().count() == 2L) {
                room3 = RoomsEntity.new {
                    roomNumber = 911
                    roomDescription = "ASD."
                    isCoedRoom = false
                    hasLovers = false
                    isQuiet = true
                    isSocial = false
                    orientation = RoomOrientation.NORTH_EAST
                    nickname = "Asd"
                    capacity = 4
                }
            }
        }

        transaction {
            if (ContactInfoEntity.all().empty()) {

                ContactInfoEntity.new {
                    contactType = ContactType.DISCORD
                    uri = "discord.gg/random"
                    user = user1!!
                }

                ContactInfoEntity.new {
                    contactType = ContactType.TELEGRAM
                    uri = "@randomtg"
                    user = user2!!
                }

                AdminsEntity.new {
                    user = user1!!
                }

                BlockedRoomsEntity.new {
                    roomNumber = 404
                    roomDescription = "Ezt a szobát nem találod."
                    isCoedRoom = false
                    hasLovers = false
                    isQuiet = true
                    isSocial = false
                    orientation = RoomOrientation.SOUTH_WEST
                    nickname = "Négy-négy"
                    capacity = 0
                }

                HiddenRoomsEntity.new {
                    hiderUser = user1!!
                    hiddenRoom = room2!!
                }

                roomMergeRequestsEntity = RoomMergeRequestsEntity.new {
                    requesterUser = user2!!
                    sourceRoom = room2!!
                    targetRoom = room1!!
                    state = ApprovalStates.PENDING
                }

                RoomMergeApprovesEntity.new {
                    roomMergeRequest = roomMergeRequestsEntity!!
                    approverUser = user2!!
                    state = ApprovalStates.PENDING
                }



                RoomReportEntity.new {
                    reportedRoom = room1!!
                    reportingUser = user2!!
                    reason = "Túl hangos!"
                    seenByAdmin = false
                }

                UserReportEntity.new {
                    reporterUser = user1!!
                    reportedUser = user2!!
                    reason = "Folyamatosan zavar!"
                    state = UserReportState.PENDING
                }

                BlockedUserEntity.new {
                    user = user2!!
                    report = UserReportEntity.find { UserReportsTable.reporterUser eq user1!!.id }.first()
                }
            }
        }

    }
    fun dropTables()
}