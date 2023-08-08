package com.jbrst.jbrstask.ui

import com.jbrst.jbrstask.api.models.BuildDto
import com.jbrst.jbrstask.api.models.NewProjectDescriptionDto
import com.jbrst.jbrstask.api.models.PropertiesDto
import com.jbrst.jbrstask.core.annotations.DesktopTest
import io.qameta.allure.*
import io.qameta.allure.SeverityLevel.CRITICAL
import org.testng.annotations.AfterMethod
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test
import strikt.api.expectThat
import strikt.assertions.isA
import strikt.assertions.isEqualTo


@Story("As a user I want to be able to manage my projects builds")
@Epic("Builds")
@Features(value = [Feature("Build execution")])
class BuildsUiTest : BaseUiTest() {

    @BeforeMethod
    fun login() {
        loginFlow.loggedAs(testData.userData.superAdmin())
        testDataStateHelper.authorizedAllAgents()
    }

    @AfterMethod
    fun unauthorizeAllAgents() {
        testDataStateHelper.unauthorizedAllAgents()

    }

    @Test
    @Severity(CRITICAL)
    @DesktopTest
    fun userIsAbleToRunBuildFromNewlyCreatedProjectTest() {
        val projectName = testData.fakeProjectName()
        val newProject = NewProjectDescriptionDto(projectName.replaceFirstChar { it.titlecaseChar() }, projectName)
        val newBuildType = testData.buildTypeEntry(newProject.name)
        val newVcsRoot = testData.buildVcsRoot(newProject.id)
        val newVscRootEntry = testData.buildVcsRootEntry(newVcsRoot)
        val newMvnStep = testData.buildTestMavenStep()

        // Create a new project via API
        projectAssistant.createProject(newProject)
        // Create a new vcs root via API
        vcsRootAssistant.addNewVcsRoot(newVcsRoot)
        // Create a new build type via API
        buildTypeAssistant.createBuildType(newBuildType)
        // Add a VCS root entry to the build type
        buildTypeAssistant.addVcsRootEntryToBuild(newBuildType.id, newVscRootEntry)
        // Create a new step for the build via API
        buildTypeAssistant.addStepToBuildType(newBuildType.id, newMvnStep)

        // Run build via UI
        buildFlow.runBuild(newProject.name, newBuildType.name)

        // Wait until the build is finished
        buildAssistant.awaitUntilBuildFinished(newBuildType.id)

        // Check that the build is succeeded
        buildAssistant.getLatestBuild(newBuildType.id).also {
            expectThat(it).isA<BuildDto>()
                .get { status }.isEqualTo("SUCCESS")
        }

        // Check the build content is correct
        buildAssistant.getBuildStatistics(newBuildType.id).also { props: PropertiesDto? ->
            expectThat(props).isA<PropertiesDto>()
                .get { property.find { it.name == "PassedTestCount" }?.value == "4" }
        }
    }

}