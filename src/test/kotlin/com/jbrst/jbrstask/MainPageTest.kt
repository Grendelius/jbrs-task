package com.jbrst.jbrstask

import com.codeborne.selenide.Condition.visible
import com.codeborne.selenide.Configuration
import com.codeborne.selenide.Selenide
import com.codeborne.selenide.Selenide.element
import com.codeborne.selenide.Selenide.open
import org.openqa.selenium.chrome.ChromeOptions
import com.codeborne.selenide.logevents.SelenideLogger
import com.jbrst.jbrstask.ui.flows.LoginFlow
import com.jbrst.jbrstask.core.models.User
import io.qameta.allure.selenide.AllureSelenide
import org.testng.annotations.*
import org.testng.Assert.*

class MainPageTest {
    private val mainPage = MainPage()

    companion object {
        @BeforeClass
        fun setUpAll() {
            Configuration.browserSize = "1280x800"
            SelenideLogger.addListener("allure", AllureSelenide())
        }
    }

    @BeforeMethod
    fun setUp() {
        // Fix the issue https://github.com/SeleniumHQ/selenium/issues/11750
        Configuration.browserCapabilities = ChromeOptions().addArguments("--remote-allow-origins=*")
        open("http://localhost:8111/")
    }

    @Test
    fun login() {
        LoginFlow().loggedAs(user = User("sa", "sa")) }

    @Test
    fun toolsMenu() {
        mainPage.toolsMenu.click()

        element("div[data-test='main-submenu']").shouldBe(visible)
    }

    @Test
    fun navigationToAllTools() {
        mainPage.seeDeveloperToolsButton.click()
        mainPage.findYourToolsButton.click()

        element("#products-page").shouldBe(visible)

        assertEquals(Selenide.title(), "All Developer Tools and Products by JetBrains")
    }
}
