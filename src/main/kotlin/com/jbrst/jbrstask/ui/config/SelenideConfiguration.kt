package com.jbrst.jbrstask.ui.config

import com.codeborne.selenide.logevents.SelenideLogger
import io.qameta.allure.selenide.AllureSelenide
import io.qameta.allure.selenide.LogType
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import java.util.logging.Level
import com.codeborne.selenide.Configuration as SelenideConfig

@Configuration
open class SelenideConfiguration(env: Environment) {

    init {
        SelenideConfig.baseUrl = env.getProperty("tc.server.url")
        SelenideConfig.headless = env.getProperty("core.browser.headless")?.toBooleanStrict() ?: false
        SelenideConfig.timeout = env.getProperty("core.browser.timeout", "10000").toLong()
        SelenideConfig.reopenBrowserOnFail = true

        val isRemoteRun = env.getProperty("core.browser.remote.active")?.toBooleanStrict() ?: false

        if (isRemoteRun) {
            SelenideConfig.remote = env.getProperty("core.browser.remote.url")
        }

        val allureSelenide = AllureSelenide().apply {
            screenshots(true)
            includeSelenideSteps(true)
            savePageSource(env.getProperty("core.allure.page-source").toBoolean())
            when {
                env.getProperty("core.allure.browser-logs").toBoolean() -> {
                    enableLogs(LogType.BROWSER, Level.ALL)
                    enableLogs(LogType.PERFORMANCE, Level.ALL)
                }
            }
        }

        SelenideLogger.addListener("AllureSelenide", allureSelenide)
    }

}