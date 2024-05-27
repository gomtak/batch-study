package com.gomtak.batch

import com.gomtak.batch.Users.age
import com.gomtak.batch.Users.city
import com.gomtak.batch.Users.id
import com.gomtak.batch.Users.name
import lombok.RequiredArgsConstructor
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.greater
import org.jetbrains.exposed.sql.SqlExpressionBuilder.greaterEq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.lessEq
import org.jetbrains.exposed.sql.statements.BatchInsertStatement
import org.jetbrains.exposed.sql.statements.BatchReplaceStatement
import org.jetbrains.exposed.sql.statements.BatchUpdateStatement
import org.jetbrains.exposed.sql.transactions.TransactionManager
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
//                addLogger(StdOutSqlLogger)  logging.level.Exposed: debug 으로 Show SQL logging 확인
//                SchemaUtils.create(Cities, Users) generate-ddl: true 으로 스키마 생성

            val toList = Users.selectAll().where { age lessEq 50 }.toList()
            Users.batchReplace(toList, false) {
                this[Users.id] = it[Users.id]
                this[name] = it[name]
                this[age] = it[age] + 50
            }
            RepeatStatus.FINISHED
        }
    }
}