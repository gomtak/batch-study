package com.gomtak.batch

import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.batch.item.ItemReader
import org.springframework.stereotype.Component

@Component
class CustomItemReader : ItemReader<User> {
    private var currentIndex = 0
    private var users: List<User>? = null
    override fun read(): User? {
        if (users == null)
            users = getUsersWithCityIdLessThan20()
        return if (currentIndex < users!!.size) {
            val user = users!![currentIndex]
            currentIndex++
            user
        } else null
    }

    private fun getUsersWithCityIdLessThan20(): List<User>? {
        return transaction {
            User.find {
                Users.city lessEq 20L
            }.toList()
        }
    }
}