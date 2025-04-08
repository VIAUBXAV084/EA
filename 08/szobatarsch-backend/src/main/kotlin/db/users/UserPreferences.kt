package hu.bme.kszk.db.users

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ReferenceOption
import java.util.*

object UserPreferencesTable : UUIDTable("UserPreferences") {
    val user = reference("user", UsersTable.id, ReferenceOption.CASCADE, ReferenceOption.CASCADE)
    val lateSleep = integer("late_sleep").nullable().check { it.between(0, 4) }
    val isSociable = integer("is_sociable").nullable().check { it.between(0, 4) }
    val nightGuest = integer("night_guest").nullable().check { it.between(0, 4) }
    val disturbedSleep = integer("disturbed_sleep").nullable().check { it.between(0, 4) }
    val muchStuff = integer("much_stuff").nullable().check { it.between(0, 4) }
    val longTimeSpentInDorm = integer("long_time_spent_in_dorm").nullable().check { it.between(0, 4) }
    val annoyedByRubbish = integer("annoyed_by_rubbish").nullable().check { it.between(0, 4) }
    val requiresFrequentCleaning = integer("requires_frequent_cleaning").nullable().check { it.between(0, 4) }
    val lotOfFridgeSpace = integer("lot_of_fridge_space").nullable().check { it.between(0, 4) }
    val bigSizeObjects = integer("big_size_objects").nullable().check { it.between(0, 4) }
    val topOrBottomBed = integer("top_or_bottom_bed").nullable().check { it.between(0, 4) }
    val mansNotHot = integer("mans_not_hot").nullable().check { it.between(0, 4) } //Mennyire szereti, hogy meleg van a szobában
    val smokey = integer("smokey").nullable().check { it.between(0, 4) }
    val drinky = integer("drinky").nullable().check { it.between(0, 4) }
}

class UserPreferencesEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<UserPreferencesEntity>(UserPreferencesTable)

    var user by UsersEntity referencedOn UserPreferencesTable.user
    var lateSleep by UserPreferencesTable.lateSleep
    var isSociable by UserPreferencesTable.isSociable
    var nightGuest by UserPreferencesTable.nightGuest
    var disturbedSleep by UserPreferencesTable.disturbedSleep
    var muchStuff by UserPreferencesTable.muchStuff
    var longTimeSpentInDorm by UserPreferencesTable.longTimeSpentInDorm
    var annoyedByRubbish by UserPreferencesTable.annoyedByRubbish
    var requiresFrequentCleaning by UserPreferencesTable.requiresFrequentCleaning
    var lotOfFridgeSpace by UserPreferencesTable.lotOfFridgeSpace
    var bigSizeObjects by UserPreferencesTable.bigSizeObjects
    var topOrBottomBed by UserPreferencesTable.topOrBottomBed
    var mansNotHot by UserPreferencesTable.mansNotHot
    var smokey by UserPreferencesTable.smokey
    var drinky by UserPreferencesTable.drinky
}