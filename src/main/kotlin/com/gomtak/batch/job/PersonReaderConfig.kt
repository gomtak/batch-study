package com.gomtak.batch.job

import com.gomtak.batch.CustomJpaPagingItemReader
import com.gomtak.batch.domain.Person
import jakarta.persistence.EntityManagerFactory
import org.springframework.batch.core.*
import org.springframework.batch.core.configuration.annotation.JobScope
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.core.job.SimpleJob
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.item.database.JpaItemWriter
import org.springframework.batch.item.database.JpaPagingItemReader
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.PlatformTransactionManager
import java.time.LocalDateTime

@Configuration
class PersonReaderConfig {
    @Bean
    fun simpleJob(jobRepository: JobRepository, simpleStep: Step): Job {
        val simpleJob = SimpleJob()
        simpleJob.addStep(simpleStep)
        simpleJob.setJobRepository(jobRepository)
        return simpleJob
    }

    @Bean
    @JobScope
    fun simpleStep(@Value("#{jobParameters[dateTime] ?: ''}") dateTime: String,
                   jobRepository: JobRepository,
                   transactionManager: PlatformTransactionManager,
                   entityManagerFactory: EntityManagerFactory): Step {
        var parse = LocalDateTime.now()
        if (dateTime.isNotEmpty()) {
            parse = LocalDateTime.parse(dateTime);
        }
        println("dateTime: $parse")
        return StepBuilder("simpleStep", jobRepository)
            .chunk<Person, Person>(10, transactionManager)
            .reader(jpaPagingItemReader(entityManagerFactory))
            .processor {doSomething(it)}
            .writer(personJpaItemWriter(entityManagerFactory))
            .build()
    }

    fun doSomething(it: Person): Person? {
        it.aging()
        return it
    }

    @Bean
    fun personJpaItemWriter(entityManagerFactory: EntityManagerFactory): JpaItemWriter<Person> {
        return JpaItemWriterBuilder<Person>()
            .entityManagerFactory(entityManagerFactory)
            .build()
    }

    @Bean
    fun jpaPagingItemReader(entityManagerFactory: EntityManagerFactory): JpaPagingItemReader<Person> {
        val customJpaPagingItemReader = CustomJpaPagingItemReader<Person>()
        customJpaPagingItemReader.setQueryString("SELECT p FROM Person p")
        customJpaPagingItemReader.currentItemCount = 0
        customJpaPagingItemReader.setEntityManagerFactory(entityManagerFactory)
        customJpaPagingItemReader.afterPropertiesSet()
        return customJpaPagingItemReader
    }
}