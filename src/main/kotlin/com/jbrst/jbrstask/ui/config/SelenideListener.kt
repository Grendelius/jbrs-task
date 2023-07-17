package com.jbrst.jbrstask.ui.config

import com.codeborne.selenide.logevents.SelenideLogger
import io.qameta.allure.selenide.AllureSelenide
import io.qameta.allure.selenide.LogType
import org.springframework.core.env.Environment
import org.springframework.stereotype.Component
import java.util.logging.Level

@Component
class SelenideListener(private val env: Environment) {

    fun init() {
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