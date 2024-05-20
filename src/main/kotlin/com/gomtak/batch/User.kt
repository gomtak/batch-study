package com.gomtak.batch

import com.gomtak.batch.User.Companion.optionalReferrersOn
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.Column

object Users : LongIdTable("user") {
    val name: Column<String> = varchar("name", 50)
    val city: Column<Long?> = long("city").references(Cities.id).nullable()
    val age: Column<Int> = integer("age")
}
object Cities: LongIdTable("city") {
    val name = varchar("name", 50)
}

class User(id: EntityID<Long>) : LongEntity(id) {
    fun aging(): User {
        age++
        return this
    }

    companion object : LongEntityClass<User>(Users)

    var name by Users.name
    var city by City optionalReferencedOn Users.city
    var age by Users.age
}

class City(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<City>(Cities)

    var name by Cities.name
    val users by User optionalReferrersOn Users.city
}