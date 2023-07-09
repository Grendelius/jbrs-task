package com.jbrst.jbrstask.ui

import com.jbrst.jbrstask.core.util.FakeData
import com.jbrst.jbrstask.ui.core.Page
import com.jbrst.jbrstask.ui.flows.LoginFlow
import com.jbrst.jbrstask.ui.flows.LoginPage
import io.qameta.allure.Severity
import io.qameta.allure.SeverityLevel.CRITICAL
import org.testng.annotations.Test

class LoginTest : BaseUiTest() {

    private val invalidCredsMsg = "Incorrect username or password."

    @Test
    @Severity(CRITICAL)
    fun superAdminUserIsAbleToLoginToServerTest() {
        LoginFlow().loggedAs(user = userData.superAdmin())
    }

    @Test
    @Severity(CRITICAL)
    fun regularUserIsAbleToLoginToServerTest() {
        loginFlow.loggedAs(user = userData.getByUsername("admin"))
    }

    @Test
    @Severity(CRITICAL)
    fun unknownUserIsNotAbleToLoginToServerTest() {
        val unknownUser = FakeData.randomUser()

        Page.on<LoginPage>()
            .inputUsername(username = unknownUser.username)
            .inputPassword(psw = unknownUser.password)
            .submit()
            .errorMessageDisplayed(invalidCredsMsg)
    }

}