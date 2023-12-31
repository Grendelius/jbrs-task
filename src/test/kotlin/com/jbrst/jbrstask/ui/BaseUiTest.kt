package com.jbrst.jbrstask.ui

import com.codeborne.selenide.Selenide
import com.jbrst.jbrstask.BaseTest
import com.jbrst.jbrstask.api.assistants.BuildApiAssistant
import com.jbrst.jbrstask.api.assistants.BuildTypeApiAssistant
import com.jbrst.jbrstask.api.assistants.ProjectApiAssistant
import com.jbrst.jbrstask.api.assistants.VcsRootApiAssistant
import com.jbrst.jbrstask.ui.config.SelenideListener
import com.jbrst.jbrstask.ui.config.WebDriverFactory
import com.jbrst.jbrstask.ui.config.WebDriverFactory.Browser
import com.jbrst.jbrstask.ui.flows.BuildFlow
import com.jbrst.jbrstask.ui.flows.LoginFlow
import com.jbrst.jbrstask.ui.flows.ProjectsFlow
import org.springframework.beans.factory.annotation.Autowired
import org.testng.annotations.AfterMethod
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Optional
import org.testng.annotations.Parameters


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

    @Autowired
    protected lateinit var projectAssistant: ProjectApiAssistant

    @Autowired
    protected lateinit var buildAssistant: BuildApiAssistant

    @Autowired
    protected lateinit var buildTypeAssistant: BuildTypeApiAssistant

    @Autowired
    protected lateinit var vcsRootAssistant: VcsRootApiAssistant

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