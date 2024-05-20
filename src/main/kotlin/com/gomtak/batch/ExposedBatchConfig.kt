package com.gomtak.batch

import com.gomtak.batch.Users.age
import com.gomtak.batch.Users.city
import lombok.RequiredArgsConstructor
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.lessEq
import org.springframework.batch.core.*
import org.springframework.batch.core.job.SimpleJob
import org.springframework.batch.core.repository.JobRepository
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
        customItemReader: CustomItemReader,
        jobRepository: JobRepository,
        transactionManager: PlatformTransactionManager
    ): Step {
        return StepBuilder("_", jobRepository)
            .chunk<User, User>(10, transactionManager)
            .reader(customItemReader)
            .processor { it.aging() }
            .writer { items -> items.forEach { println(it.name) } }
            .build()
    }
}