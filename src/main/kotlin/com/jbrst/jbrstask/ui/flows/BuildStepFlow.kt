package com.jbrst.jbrstask.ui.flows

import com.codeborne.selenide.ClickOptions
import com.codeborne.selenide.CollectionCondition
import com.codeborne.selenide.Condition.*
import com.codeborne.selenide.Selectors.*
import com.codeborne.selenide.Selenide
import com.codeborne.selenide.Selenide.element
import com.codeborne.selenide.Selenide.elements
import com.jbrst.jbrstask.ui.core.Page
import com.jbrst.jbrstask.ui.core.Validatable
import com.jbrst.jbrstask.ui.flows.BuildConfigurationPage.Companion.runBuildBtn
import io.qameta.allure.Step
import mu.KLogging
import org.springframework.stereotype.Component

@Component
class BuildFlow {

    @Step("Open the Build page")
    fun openBuild(projectName: String, buildName: String): BuildConfigurationPage {
        Selenide.open("/buildConfiguration/${projectName}_$buildName")
        return Page.on<BuildConfigurationPage>()
    }

    @Step("Run the Build")
    fun runBuild(projectName: String, buildName: String): BuildConfigurationPage {
        Selenide.open("/buildConfiguration/${projectName}_$buildName")
        return Page.on<BuildConfigurationPage>()
            .clickOn(runBuildBtn)
            .on<BuildConfigurationPage>()
    }

}

class BuildConfigurationPage : Page() {

    companion object : KLogging() {
        private val tabsContainer = element(byClassName("ring-tabs-autoCollapseContainer"))
        internal val runBuildBtn = element(byAttribute("data-test", "run-build"))
    }

    override fun validate(): Validatable {
        EditBuildRunnersPage.logger.debug { " Checking the Build Configuration page is open." }
        tabsContainer.shouldBe(visible)
        return this
    }

}

class EditBuildRunnersPage : Page() {

    companion object : KLogging() {
        private val pageHeader = element(byText("Build Steps"))
        private val successMessage = element(byId("unprocessed_buildRunnerSettingsUpdated"))
    }

    fun successMessageIsDisplayed(msg: String): EditBuildRunnersPage {
        successMessage.shouldHave(ownText(msg))
        return this
    }

    override fun validate(): Validatable {
        logger.debug { " Checking the Edit Build Runners page is open." }
        pageHeader.shouldBe(visible)
        return this
    }

}

class DiscoverRunnersPage : Page() {

    companion object : KLogging() {
        private val successMessage = element(byId("unprocessed_objectsCreated"))
        private val progressContainer = element(byId("discoveryProgressContainer"))
        internal val buildStepCheckboxes = elements(byId("runnerId"))
        internal val useSelectedStepsBtn = element(byXpath("//a[text() = 'Use selected']"))
    }

    fun waitForVcsProgressingLoader(): DiscoverRunnersPage {
        progressContainer.shouldNotBe(visible)
        return this
    }

    fun successMessageIsDisplayed(msg: String): DiscoverRunnersPage {
        successMessage.shouldBe(text(msg))
        return this
    }

    fun chooseAutoDetectedStep(stepIndex: Int): DiscoverRunnersPage {
        buildStepCheckboxes
            .shouldBe(CollectionCondition.sizeGreaterThan(0))
            .toList()[stepIndex]
            .click(ClickOptions.usingDefaultMethod())
            .shouldBe(checked)
        return this
    }

    fun useSelectedSteps(): Page {
        useSelectedStepsBtn.hover().click()
        return this
    }

    override fun validate(): Validatable {
        logger.info { "Checking the Build Steps Page is open. " }
        return super.validate()
    }

}