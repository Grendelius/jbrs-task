package com.jbrst.jbrstask

import com.jbrst.jbrstask.api.ApiServiceCreator
import com.jbrst.jbrstask.api.TestDataStateHelper
import com.jbrst.jbrstask.core.listeners.CustomInterceptor
import com.jbrst.jbrstask.core.listeners.LogListener
import com.jbrst.jbrstask.core.testdata.TestDataProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests
import org.testng.annotations.AfterSuite
import org.testng.annotations.Listeners

@Listeners(value = [LogListener::class, CustomInterceptor::class])
@SpringBootTest(classes = [SpringTestApplication::class], webEnvironment = WebEnvironment.NONE)
open class BaseTest : AbstractTestNGSpringContextTests() {

    @Autowired
    protected lateinit var testData: TestDataProvider

    @Autowired
    protected lateinit var apiServiceCreator: ApiServiceCreator

    @Autowired
    protected lateinit var testDataStateHelper: TestDataStateHelper

    @AfterSuite
    fun cleanup() {
        testDataStateHelper.cleanCreatedProjects()
        testDataStateHelper.cleanExtraUsers()
    }

}