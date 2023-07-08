package com.jbrst.jbrstask

import com.codeborne.selenide.Configuration
import com.codeborne.selenide.Selenide.open
import com.codeborne.selenide.logevents.SelenideLogger
import com.jbrst.jbrstask.core.models.User
import com.jbrst.jbrstask.ui.flows.LoginFlow
import io.qameta.allure.selenide.AllureSelenide
import org.openqa.selenium.chrome.ChromeOptions
import org.testng.annotations.BeforeClass
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

class ExperimentalTest: BaseTest() {

    companion object {

        @BeforeClass
        fun setUpAll() {
            Configuration.browserSize = "1280x800"
            SelenideLogger.addListener("allure", AllureSelenide())
        }

    }

    @BeforeMethod
    fun setUpBrowser() {
        Configuration.browserCapabilities = ChromeOptions().addArguments("--remote-allow-origins=*")
        open("http://localhost:8111/")
    }

    @Test
    fun login() {
        LoginFlow().loggedAs(user = User("admin", "admin"))
    }


}
