package com.jbrst.jbrstask

import com.codeborne.selenide.Configuration
import com.codeborne.selenide.Selenide.open
import com.jbrst.jbrstask.core.models.User
import com.jbrst.jbrstask.ui.flows.LoginFlow
import org.openqa.selenium.chrome.ChromeOptions
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

class ExperimentalTest: BaseTest() {

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
