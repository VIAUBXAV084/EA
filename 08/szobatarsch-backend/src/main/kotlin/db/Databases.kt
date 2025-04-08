package hu.bme.kszk.db


import hu.bme.kszk.db.rooms.*
import hu.bme.kszk.db.users.*
import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

const val MAX_VARCHAR_LENGTH = 100

object DatabaseFactory : DatabaseInterface {
    override val db: Database

    //TODO false for in-memory db
    private const val IS_DOCKER_DB = true

    private fun connectInMemoryDb() =  Database.connect("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", driver = "org.h2.Driver")

    private fun connectDockerDb() =  Database.connect(
        url = "jdbc:postgresql://"+System.getenv("POSTGRES_HOST")+":"+System.getenv("POSTGRES_PORT")+"/"+System.getenv("POSTGRES_DB")+"?sslmode=disable",
        driver = System.getenv("DB_DRIVER") ?: "org.postgresql.Driver",
        user = System.getenv("POSTGRES_USER"),
        password = System.getenv("POSTGRES_PASSWORD")
    )


    private fun connect() = if(IS_DOCKER_DB) connectDockerDb() else connectInMemoryDb()

    init {
        db = connect()
        transaction {

            SchemaUtils.create(UsersTable)
            SchemaUtils.create(UserReportsTable)
            SchemaUtils.create(UserPreferencesTable)
            SchemaUtils.create(ContactInfosTable)
            SchemaUtils.create(BrosTable)
            SchemaUtils.create(BlockedUsersTable)
            SchemaUtils.create(AdminsTable)

            SchemaUtils.create(RoomMergeRequestsTable)
            SchemaUtils.create(RoomMergeApprovesTable)
            SchemaUtils.create(BlockedRoomsTable)
            SchemaUtils.create(HiddenRoomsTable)

            SchemaUtils.create(RoomInviteRequestsTable)
            SchemaUtils.create(RoomInviteApprovesTable)
            SchemaUtils.create(RoomJoinRequestsTable)
            SchemaUtils.create(RoomJoinApprovesTable)

            SchemaUtils.create(RoomsTable)
            SchemaUtils.create(RoomReportsTable)
        }
    }



    override fun dropTables() {
        transaction {
            SchemaUtils.drop(UserPreferencesTable)
            SchemaUtils.drop(ContactInfosTable)
            SchemaUtils.drop(BrosTable)
            SchemaUtils.drop(BlockedUsersTable)
            SchemaUtils.drop(AdminsTable)
            SchemaUtils.drop(UserReportsTable)
            SchemaUtils.drop(RoomMergeRequestsTable)
            SchemaUtils.drop(RoomMergeApprovesTable)
            SchemaUtils.drop(BlockedRoomsTable)
            SchemaUtils.drop(HiddenRoomsTable)

            SchemaUtils.drop(RoomReportsTable)

            SchemaUtils.drop(RoomJoinApprovesTable)
            SchemaUtils.drop(RoomJoinRequestsTable)
            SchemaUtils.drop(RoomInviteApprovesTable)
            SchemaUtils.drop(RoomInviteRequestsTable)

            SchemaUtils.drop(UsersTable)
            SchemaUtils.drop(RoomsTable)
        }
    }
}



fun Application.configureDatabases(databaseImpl: DatabaseInterface) {
    //databaseImpl.dropTables()
    databaseImpl.createSampleData()
}
