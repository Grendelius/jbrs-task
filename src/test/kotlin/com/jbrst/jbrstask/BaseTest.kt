package com.jbrst.jbrstask

import com.jbrst.jbrstask.api.assistants.ApiServiceCreator
import com.jbrst.jbrstask.api.assistants.TestDataStateHelper
import com.jbrst.jbrstask.core.listeners.CustomInterceptor
import com.jbrst.jbrstask.core.listeners.LogListener
import com.jbrst.jbrstask.core.models.User
import com.jbrst.jbrstask.core.testdata.TestDataProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests
import org.testng.annotations.BeforeClass
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

    protected lateinit var admin: User
    protected lateinit var superAdmin: User

    @BeforeClass
    fun setUpUsers() {
        admin = testData.userData.admin()
        superAdmin = testData.userData.superAdmin()
    }

}