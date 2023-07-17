package com.jbrst.jbrstask.api

import com.jbrst.jbrstask.BaseTest
import org.testng.annotations.AfterSuite

open class BaseApiTest : BaseTest() {

    @AfterSuite
    fun cleanup() {
        testDataStateHelper.cleanCreatedProjects(admin)
        testDataStateHelper.cleanExtraUsers(admin)
    }


}