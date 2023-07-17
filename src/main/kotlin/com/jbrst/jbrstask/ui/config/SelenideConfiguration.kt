package com.jbrst.jbrstask.ui.config

import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
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
    }

}