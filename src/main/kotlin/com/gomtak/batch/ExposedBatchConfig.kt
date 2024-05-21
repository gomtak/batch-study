package com.gomtak.batch

import com.gomtak.batch.Users.age
import com.gomtak.batch.Users.city
import lombok.RequiredArgsConstructor
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.lessEq
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.batch.core.*
import org.springframework.batch.core.job.SimpleJob
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.scope.context.ChunkContext
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.item.ItemReader
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.PlatformTransactionManager
import java.util.Random
import javax.sql.DataSource

@Configuration
class ExposedBatchConfig {
    @Bean
    fun simpleJob(jobRepository: JobRepository, simpleStep: Step): Job {
        val simpleJob = SimpleJob()
        simpleJob.addStep(simpleStep)
        simpleJob.setJobRepository(jobRepository)
        return simpleJob
    }

    @Bean
    fun simpleStep(
        test: Tasklet,
        jobRepository: JobRepository,
        transactionManager: PlatformTransactionManager
    ): Step {
        return StepBuilder("_", jobRepository)
            .tasklet(test, transactionManager)
            .build()
    }

    @Bean
    fun test(dataSource: DataSource): Tasklet {
        return Tasklet { contribution: StepContribution, chunkContext: ChunkContext ->
            Database.connect(dataSource)
            transaction {
                SchemaUtils.create(Users)
                Users.insert {
                    it[name] = "Alice"
                    it[age] = 20
                    it[city] = 1L
                }
                Users.insert {
                    it[name] = "Bob"
                    it[age] = 30
                    it[city] = 2L
                }
            }
            RepeatStatus.FINISHED
        }
    }
}