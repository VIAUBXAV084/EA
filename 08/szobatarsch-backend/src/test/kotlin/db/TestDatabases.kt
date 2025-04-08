package hu.bme.kszk.db


import hu.bme.kszk.db.rooms.*
import hu.bme.kszk.db.users.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

object TestDatabases : DatabaseInterface {
    override val db: Database

    private fun connect() =  Database.connect("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", driver = "org.h2.Driver")

    init {
        db = connect()
        transaction {

            SchemaUtils.create(RoomsTable)
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

            SchemaUtils.create(RoomReportsTable)
        }
        createSampleData()

    }

    override fun dropTables() {
        TODO("Not yet implemented")
    }

}