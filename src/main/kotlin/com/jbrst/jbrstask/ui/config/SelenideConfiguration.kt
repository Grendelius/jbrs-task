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
        SelenideConfig.headless = env.getProperty("core.browser.headless")?.toBooleanStrict() ?: false
        SelenideConfig.timeout = env.getProperty("core.browser.timeout", "4500").toLong()
        SelenideConfig.reopenBrowserOnFail = true

        val allureSelenide = AllureSelenide().apply {
            screenshots(true)
                .includeSelenideSteps(false)
                .savePageSource(env.getProperty("core.allure.page-source").toBoolean())
            when {
                env.getProperty("core.allure.browser-logs").toBoolean() -> {
                    enableLogs(LogType.BROWSER, Level.ALL)
                    enableLogs(LogType.PERFORMANCE, Level.ALL)
                }
            }
        }

        SelenideLogger.addListener("allure", allureSelenide)
    }

}