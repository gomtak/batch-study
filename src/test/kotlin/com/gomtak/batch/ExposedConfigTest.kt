package com.gomtak.batch

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.concurrent.Flow.Publisher
import javax.sql.DataSource

@SpringBootTest
class ExposedConfigTest: ExposedTestSupport() {

    @Autowired
    lateinit var dataSource: DataSource
    @Test
    fun writerTest() {
        var connect = Database.connect(dataSource)
        transaction(connect) {
            var city = City.new {
                this.name = "Seoul"
            }
            val user = User.new {
                this.name = "gomtak"
                this.city = city
            }
        }
    }
}

