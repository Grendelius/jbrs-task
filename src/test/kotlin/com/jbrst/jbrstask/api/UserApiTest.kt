package com.jbrst.jbrstask.api

import com.jbrst.jbrstask.api.models.NewProjectDescriptionDto
import com.jbrst.jbrstask.api.models.toUser
import io.qameta.allure.*
import io.qameta.allure.SeverityLevel.CRITICAL
import org.apache.hc.core5.http.HttpStatus.SC_OK
import org.testng.annotations.AfterMethod
import org.testng.annotations.BeforeClass
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test
import strikt.api.expectThat
import strikt.assertions.isA
import strikt.assertions.isEqualTo
import strikt.assertions.isGreaterThan

@Story("As a user I want to be able to manage with a server's user accounts")
@Epic("Administration")
@Features(value = [Feature("User Management")])
class UserApiTest : BaseApiTest() {

    companion object {
        private lateinit var userApi: UsersApi
        private lateinit var projectsApi: ProjectApi
    }

    @BeforeClass
    fun initServicesAndTestData() {
        userApi = apiServiceCreator.createService(UsersApi::class.java, superAdmin)
        projectsApi = apiServiceCreator.createService(ProjectApi::class.java, superAdmin)
    }

    @BeforeMethod
    fun createProject() {
        projectsApi.addProject(NewProjectDescriptionDto("UserCheck", "userCheckName"))
    }

    @Test
    @Severity(CRITICAL)
    fun userIsAbleToCreateANewUserAccountTest() {
        // Generate a new user with Admin role
        val newUser = testData.newAdminUser()
        // Create a new user via API
        val createResponse = userApi.addUser(newUser).execute()

        // Check that the response is OK
        expectThat(createResponse.code()) {
            isA<Int>()
            isEqualTo(SC_OK)
        }

        // Check that the new user is able to login. List Projects as the new user
        val api: ProjectApi = apiServiceCreator.createService(ProjectApi::class.java, newUser.toUser())
        val listProjectsResponse = api.listProjects().execute()

        expectThat(listProjectsResponse.body()?.count) {
            isA<Int>().isGreaterThan(0)
        }
    }

}