package com.jbrst.jbrstask.ui.flows

import com.codeborne.selenide.Condition.ownText
import com.codeborne.selenide.Condition.visible
import com.codeborne.selenide.Selectors.byId
import com.codeborne.selenide.Selectors.byXpath
import com.codeborne.selenide.Selenide.element
import com.jbrst.jbrstask.core.models.User
import com.jbrst.jbrstask.ui.core.Page
import mu.KLogging

class LoginFlow {

    companion object : KLogging()

    fun loggedAs(user: User) {
        logger.info { "Logging in as the '${user.username}' user" }
        Page.on<LoginPage>()
            .inputUsername(user.username)
            .inputPassword(user.password)
            .submit()
            .on<ProjectsPage>()
    }

}

class LoginPage : Page() {

    companion object : KLogging() {
        private val header = element(byId("header"))
        private val usernameInput = element(byId("username"))
        private val passwordInput = element(byId("password"))
        private val loginBtn = element(byXpath("//input[@name='submitLogin']"))
        private val rememberMeCheckBox = element(byId("remember"))
        private val errMsgBlock = element(byId("errorMessage"))
    }

    fun inputUsername(username: String): LoginPage {
        logger.debug { "Inputting username: $username" }
        usernameInput.`val`(username)
        return this
    }

    fun inputPassword(psw: String): LoginPage {
        logger.debug { "Inputting password" }
        passwordInput.`val`(psw)
        return this
    }

    fun submit(): LoginPage {
        logger.debug { "Clicking on the Log In button" }
        loginBtn.click()
        return this
    }

    fun errorMessageDisplayed(errorMsgText: String): LoginPage {
        errMsgBlock.shouldHave(ownText(errorMsgText))
        return this
    }

    override fun validate(): LoginPage {
        logger.debug { "Checking the login page is opened" }
        header.shouldBe(visible)
        return this
    }

}