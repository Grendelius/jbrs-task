package com.jbrst.jbrstask.ui

import com.jbrst.jbrstask.ui.core.Page
import com.jbrst.jbrstask.ui.flows.LoginPage
import io.qameta.allure.Epic
import io.qameta.allure.Feature
import io.qameta.allure.Severity
import io.qameta.allure.SeverityLevel.CRITICAL
import org.testng.annotations.Test

@Epic("Authorization")
@Feature("Login")
class LoginUiTest : BaseUiTest() {

    @Test
    @Severity(CRITICAL)
    fun superAdminUserIsAbleToLoginToServerTest() {
        loginFlow.loggedAs(user = superAdmin)
    }

    @Test
    @Severity(CRITICAL)
    fun regularUserIsAbleToLoginToServerTest() {
        loginFlow.loggedAs(user = admin)
    }

    @Test
    @Severity(CRITICAL)
    fun unknownUserIsNotAbleToLoginToServerTest() {
        val unknownUser = testData.fakeUser()
        val invalidCredsMsg = "Incorrect username or password."

        Page.on<LoginPage>()
            .inputUsername(username = unknownUser.username)
            .inputPassword(psw = unknownUser.password)
            .submit()
            .errorMessageIsDisplayed(invalidCredsMsg)
    }

}