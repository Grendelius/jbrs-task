package com.jbrst.jbrstask.ui

import com.codeborne.selenide.Configuration
import com.codeborne.selenide.Selenide
import com.jbrst.jbrstask.BaseTest
import com.jbrst.jbrstask.core.UserData
import com.jbrst.jbrstask.ui.flows.LoginFlow
import org.openqa.selenium.chrome.ChromeOptions
import org.springframework.beans.factory.annotation.Autowired
import org.testng.annotations.AfterMethod
import org.testng.annotations.BeforeMethod

open class BaseUiTest : BaseTest() {

    @Autowired
    protected lateinit var userData: UserData

    @Autowired
    protected lateinit var loginFlow: LoginFlow

    @BeforeMethod
    fun setUpBrowser() {
        Configuration.browserCapabilities = ChromeOptions().addArguments("--remote-allow-origins=*")
        Selenide.open("http://localhost:8111/")
    }

    @AfterMethod
    fun closeBrowser() {
        Selenide.closeWebDriver()
    }

}