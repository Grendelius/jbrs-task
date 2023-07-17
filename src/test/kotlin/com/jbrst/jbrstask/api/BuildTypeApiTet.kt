package com.jbrst.jbrstask.api

import com.jbrst.jbrstask.api.models.Id
import io.qameta.allure.*
import io.qameta.allure.SeverityLevel.CRITICAL
import org.testng.annotations.BeforeClass
import org.testng.annotations.Test

@Story("As a user I want to be able to manage and run the project builds")
@Epic("Builds")
@Features(value = [Feature("Builds Management")])
class BuildTypeApiTet : BaseApiTest() {

    companion object {
        private lateinit var projectApi: ProjectApi
        private lateinit var userApi: UsersApi
    }

    @BeforeClass
    fun initServicesAndTestData() {
        projectApi = apiServiceCreator.createService(ProjectApi::class.java, admin)
        userApi = apiServiceCreator.createService(UsersApi::class.java, admin)
    }

}