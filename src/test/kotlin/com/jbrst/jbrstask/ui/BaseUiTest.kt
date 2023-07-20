package com.jbrst.jbrstask.ui

import com.codeborne.selenide.Selenide
import com.jbrst.jbrstask.BaseTest
import com.jbrst.jbrstask.ui.config.SelenideListener
import com.jbrst.jbrstask.ui.config.WebDriverFactory
import com.jbrst.jbrstask.ui.config.WebDriverFactory.Browser
import com.jbrst.jbrstask.ui.flows.BuildFlow
import com.jbrst.jbrstask.ui.flows.LoginFlow
import com.jbrst.jbrstask.ui.flows.ProjectsFlow
import org.springframework.beans.factory.annotation.Autowired
import org.testng.annotations.*


open class BaseUiTest : BaseTest() {

    @Autowired
    private lateinit var selenideListener: SelenideListener

    @Autowired
    private lateinit var webDriverFactory: WebDriverFactory

    @Autowired
    protected lateinit var loginFlow: LoginFlow

    @Autowired
    protected lateinit var projectsFlow: ProjectsFlow

    @Autowired
    protected lateinit var buildFlow: BuildFlow

    @BeforeMethod
    @Parameters("browser")
    fun manageWebDriver(@Optional browser: Browser?) {
        selenideListener.init()
        webDriverFactory.initWebDriver(browser)
    }

    @BeforeMethod
    fun openSut() {
        Selenide.open("/")
    }

    @AfterMethod
    fun closeBrowser() {
        Selenide.closeWebDriver()
    }

}