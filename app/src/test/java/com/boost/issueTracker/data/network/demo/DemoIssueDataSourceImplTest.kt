package com.boost.issueTracker.data.network.demo

import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.junit.Assert.*
import org.junit.Before

import org.junit.Test

class DemoIssueDataSourceImplTest {

    private lateinit var demoIssueDataSourceImpl: DemoIssueDataSourceImpl

    @Before
    fun setUp() {
        demoIssueDataSourceImpl = DemoIssueDataSourceImpl(json = Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
        })
    }

    @Test
    fun receiveIssues() {
        runBlocking {
            val result = demoIssueDataSourceImpl.receiveIssues()
            assertNotNull(result)
            assertEquals(result[0].title, "로그아웃 버튼 활성화")
        }
    }
}