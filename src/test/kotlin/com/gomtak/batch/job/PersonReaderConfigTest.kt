package com.gomtak.batch.job

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class PersonReaderConfigTest {
    @Test
    fun parseTest() {
        val dateTime = "2021-08-01T00:00:00"
        val parse = LocalDateTime.parse(dateTime)
        println("dateTime: $parse")
        assertEquals(parse, LocalDateTime.of(2021, 8, 1, 0, 0, 0))
    }
}