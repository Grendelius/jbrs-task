package com.jbrst.jbrstask.api

import com.jbrst.jbrstask.api.client.ProjectApi
import com.jbrst.jbrstask.api.models.NewProjectDescriptionDto
import com.jbrst.jbrstask.api.models.toUser
import io.qameta.allure.*
import io.qameta.allure.SeverityLevel.CRITICAL
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test
import strikt.api.expectThat
import strikt.assertions.isA
import strikt.assertions.isGreaterThan

@Story("As a user I want to be able to manage with a server's user accounts")
@Epic("Administration")
@Features(value = [Feature("User Management")])
class UserApiTest : BaseApiTest() {

    @BeforeMethod
    fun createProject() {
        projectAssistant.createProject(NewProjectDescriptionDto("UserCheck", "userCheckName"))
    }

    @Test
    @Severity(CRITICAL)
    fun userIsAbleToCreateANewUserAccountTest() {
        // Generate a new user with Admin role
        val newUser = testData.newAdminUser()
        // Create a new user via API
        userAssistant.addNewUserAccount(newUser)

        // Check that the new user is able to login. List Projects as the new user
        val api: ProjectApi = apiServiceCreator.createService(ProjectApi::class.java, newUser.toUser())
        val listProjectsResponse = api.listProjects().execute()

        expectThat(listProjectsResponse.body()?.count) {
            isA<Int>().isGreaterThan(0)
        }
    }

}