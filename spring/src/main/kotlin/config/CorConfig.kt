package com.crowdproj.rating.spring.config

import com.crowdproj.rating.biz.CwpRatingProcessor
import com.crowdproj.rating.common.CwpRatingCorSettings
import com.crowdproj.rating.common.repo.IRatingRepository
import com.crowdproj.rating.logging.common.CwpLoggerProvider
import com.crowdproj.rating.logging.logback.ratingLoggerLogback
import com.crowdproj.rating.repo.inmemory.RatingRepoInMemory
import com.crowdproj.rating.repo.postgresql.RepoRatingSQL
import com.crowdproj.rating.repo.postgresql.SqlProperties
import com.crowdproj.rating.repo.stubs.RatingRepoStub
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(SqlPropertiesEx::class)
class CorConfig {
    @Bean
    fun loggerProvider(): CwpLoggerProvider = CwpLoggerProvider { ratingLoggerLogback(it) }

    @Bean
    fun prodRepository(sqlProperties: SqlProperties) = RepoRatingSQL(sqlProperties)

    @Bean
    fun testRepository() = RatingRepoInMemory()

    @Bean
    fun stubRepository() = RatingRepoStub()

    @Bean
    fun corSettings(
        @Qualifier("prodRepository") prodRepository: IRatingRepository,
        @Qualifier("testRepository") testRepository: IRatingRepository,
        @Qualifier("stubRepository") stubRepository: IRatingRepository,
    ): CwpRatingCorSettings = CwpRatingCorSettings(
        loggerProvider = loggerProvider(),
        repoProd = prodRepository,
        repoTest = testRepository,
        repoStub = stubRepository
    )

    @Bean
    fun cwpRatingProcessor(corSettings: CwpRatingCorSettings) = CwpRatingProcessor(settings = corSettings)
}