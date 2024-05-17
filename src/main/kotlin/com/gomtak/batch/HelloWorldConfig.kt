package com.gomtak.batch

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
class HelloWorldConfig {
    @Bean
    fun jobExecution(jobLauncher: JobLauncher, simpleJob: Job): JobExecution {
        return jobLauncher.run(simpleJob, JobParameters())
    }
    @Bean
    fun simpleJob(jobRepository: JobRepository, simpleStep: Step): Job {
        val simpleJob = SimpleJob("simpleJob")
        simpleJob.addStep(simpleStep)
        simpleJob.setJobRepository(jobRepository)
        return simpleJob
    }

    @Bean
    fun simpleStep1(jobRepository: JobRepository, transactionManager: PlatformTransactionManager): Step {
        val simpleStep1 = TaskletStep("simpleStep1")
        simpleStep1.setJobRepository(jobRepository)
        simpleStep1.setTransactionManager(transactionManager)
        simpleStep1.tasklet = Tasklet { contribution: StepContribution?, chunkContext: ChunkContext? ->
            println(">>>>> This is Step1")
            RepeatStatus.FINISHED
        }
        return simpleStep1
    }
}