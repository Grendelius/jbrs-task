package com.jbrst.jbrstask.ui.config

import com.codeborne.selenide.SelenideConfig
import com.codeborne.selenide.SelenideDriver
import com.codeborne.selenide.WebDriverRunner
import com.jbrst.jbrstask.ui.MobileDevice
import com.jbrst.jbrstask.ui.MobileDevice.PIXEL7
import com.jbrst.jbrstask.ui.config.WebDriverFactory.Browser.*
import mu.KLogging
import org.openqa.selenium.Capabilities
import org.openqa.selenium.Dimension
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.chrome.ChromeOptions.CAPABILITY
import org.openqa.selenium.edge.EdgeOptions
import org.openqa.selenium.firefox.FirefoxOptions
import org.openqa.selenium.remote.AbstractDriverOptions
import org.openqa.selenium.remote.CapabilityType.*
import org.openqa.selenium.remote.DesiredCapabilities
import org.openqa.selenium.remote.RemoteWebDriver
import org.openqa.selenium.safari.SafariOptions
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.net.URI
import java.time.LocalDateTime
import java.util.*

/**
 * Creates and set up a custom web driver which is based on a Browser
 */
@Component
class WebDriverFactory {

    @Value("\${core.browser.remote.url}")
    private lateinit var remoteUrl: String

    @Value("\${core.browser.remote.video-recording}")
    private lateinit var videoRecordingIsEnabled: String

    companion object : KLogging()

    enum class Browser(val dimension: Dimension? = null, val version: String? = null) {

        CHROME(Dimension(1920, 1080)),
        FIREFOX(Dimension(1366, 768)),
        EDGE(Dimension(1280, 1240)),
        SAFARI(Dimension(1536, 864)),
        CHROME_MOBILE;

        fun browserName() = this.name.lowercase()
    }

    fun initWebDriver(browser: Browser?) {
        logger.info { "Driver initialization..." }
        when (browser) {
            CHROME -> setupWebDriverManager(remoteDriver(chromeOptions()), CHROME)
            CHROME_MOBILE -> setupWebDriverManager(remoteDriver(chromeMobileOptions()), CHROME_MOBILE)
            SAFARI -> setupWebDriverManager(remoteDriver(safariOptions()), SAFARI)
            FIREFOX -> setupWebDriverManager(remoteDriver(firefoxOptions()), FIREFOX)
            EDGE -> setupWebDriverManager(remoteDriver(edgeOptions()), EDGE)
            else -> setupWebDriverManager(remoteDriver())
        }
    }

    private fun chromeOptions(): ChromeOptions {
        logger.info { "Getting ChromeOptions..." }
        return ChromeOptions().apply {
            addArguments(
                "--remote-allow-origins=*",
                "--no-sandbox",
                "--disable-gpu",
                "--disable-extensions",
                "--disable-dev-shm-usage"
            )
            setCapability(CAPABILITY, this)
            merge(basicCapabilities(CHROME.browserName()))
            addSelenoidOptions(selenoidOptions())
        }
    }

    private fun safariOptions(): SafariOptions {
        logger.info { "Getting SafariOptions..." }
        return SafariOptions().apply {
            merge(basicCapabilities(SAFARI.browserName()))
        }
    }

    private fun firefoxOptions(): FirefoxOptions {
        logger.info { "Getting FirefoxOptions..." }
        return FirefoxOptions().apply {
            merge(basicCapabilities(FIREFOX.browserName()))
            addSelenoidOptions(selenoidOptions())
//            setCapability("acceptInsecureCerts", true)
        }
    }

    private fun edgeOptions(): EdgeOptions {
        logger.info { "Getting EdgeOptions..." }
        return EdgeOptions().apply {
            merge(basicCapabilities("MicrosoftEdge"))
            addSelenoidOptions(selenoidOptions())
        }
    }

    private fun chromeMobileOptions(mobileDevice: MobileDevice = PIXEL7): ChromeOptions {
        logger.info { "Getting ChromeMobileOptions..." }
        val me = mapOf(
            "deviceMetrics" to mobileDevice.deviceMetrics,
            "userAgent" to mobileDevice.userAgent
        )
        return ChromeOptions().apply {
            setExperimentalOption("mobileEmulation", me)
            addSelenoidOptions(selenoidOptions())
            merge(basicCapabilities(CHROME.browserName()))
            setCapability(ACCEPT_INSECURE_CERTS, true)
            setCapability(CAPABILITY, this)
        }
    }

    private fun selenoidOptions(): Map<String, Any> =
        mapOf(
            "enableVnc" to true,
            "enableVideo" to videoRecordingIsEnabled.toBooleanStrict(),
            "videoName" to "test_run_${LocalDateTime.now()}"
        )

    private fun basicCapabilities(browserName: String): Capabilities {
        logger.info { "Getting Basic Capabilities..." }

        return DesiredCapabilities().apply {
            setCapability(BROWSER_NAME, browserName)
            setCapability(BROWSER_VERSION, browserVersion)
        }
    }

    private fun setupWebDriverManager(webDriver: WebDriver, browser: Browser = CHROME) {
        logger.info { "Setup webDriverRunner..." }
        WebDriverRunner.setWebDriver(SelenideDriver(SelenideConfig(), webDriver, null).webDriver)
        browser.dimension?.let { WebDriverRunner.getWebDriver().manage().window().size = it }
    }

    private fun remoteDriver(capabilities: Capabilities = chromeOptions()): RemoteWebDriver {
        logger.info { "Creating of a remote webdriver..." }
        logger.info { "Device capabilities: $capabilities" }
        return RemoteWebDriver(URI.create(remoteUrl).toURL(), capabilities)
    }

    private infix fun <T : AbstractDriverOptions<*>> T.addSelenoidOptions(selenoidOptions: Map<String, Any>) {
        setCapability("selenoid:options", selenoidOptions)
    }

}
