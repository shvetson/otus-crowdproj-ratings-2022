package com.crowdproj.rating.spring

import com.crowdproj.rating.repo.postgresql.RepoRatingSQL
import com.ninjasquad.springmockk.MockkBean
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class ApplicationTests {
    @MockkBean
    private lateinit var repo: RepoRatingSQL

    @Test
    fun contextLoads() {
    }
}
