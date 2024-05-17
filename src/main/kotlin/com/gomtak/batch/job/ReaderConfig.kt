package com.gomtak.batch.job

import org.springframework.batch.core.*
import org.springframework.batch.core.job.SimpleJob
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.scope.context.ChunkContext
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.core.step.tasklet.TaskletStep
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.PlatformTransactionManager

@Configuration
class ReaderConfig {

    @Bean
    fun readerJob(jobRepository: JobRepository, readerStep: Step): Job {
        val simpleJob = SimpleJob("readerJob")
        simpleJob.addStep(readerStep)
        simpleJob.setJobRepository(jobRepository)
        return simpleJob
    }

    @Bean
    fun readerStep(jobRepository: JobRepository, transactionManager: PlatformTransactionManager): Step {
        val simpleStep1 = TaskletStep("readerStep2")
        simpleStep1.setJobRepository(jobRepository)
        simpleStep1.setTransactionManager(transactionManager)
        simpleStep1.tasklet = tasklet()
        return simpleStep1
    }

    fun tasklet() = Tasklet { contribution: StepContribution?, chunkContext: ChunkContext? ->
        println(">>>>> This is Step2")
        RepeatStatus.FINISHED
    }
}