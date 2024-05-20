package com.gomtak.batch.domain

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import lombok.Getter

@Getter
@Entity
class Person {
    fun aging() {
        println("Aging person: $name")
        age = age?.plus(1)
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
    var name: String? = null
    var age: Int? = null
}