package com.jbrst.jbrstask.ui

import com.jbrst.jbrstask.api.*
import com.jbrst.jbrstask.api.models.*
import com.jbrst.jbrstask.core.annotations.DesktopTest
import io.qameta.allure.*
import io.qameta.allure.SeverityLevel.CRITICAL
import org.testng.annotations.AfterMethod
import org.testng.annotations.BeforeClass
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test
import strikt.api.expectThat
import strikt.assertions.isA
import strikt.assertions.isEqualTo


@Story("As a user I want to be able to manage my projects builds")
@Epic("Builds")
@Features(value = [Feature("Build execution")])
class BuildsUiTest : BaseUiTest() {


    companion object {
        private lateinit var projectApi: ProjectApi
        private lateinit var buildApi: BuildApi
        private lateinit var vcsRootApi: VcsRootApi
        private lateinit var buildTypeApi: BuildTypeApi
    }

    @BeforeClass
    fun initServicesAndTestUsers() {
        projectApi = apiServiceCreator.createService(ProjectApi::class.java, admin)
        buildApi = apiServiceCreator.createService(BuildApi::class.java, admin)
        vcsRootApi = apiServiceCreator.createService(VcsRootApi::class.java, admin)
        buildTypeApi = apiServiceCreator.createService(BuildTypeApi::class.java, admin)
    }

    @BeforeMethod
    fun login() {
        loginFlow.loggedAs(admin)
        testDataStateHelper.authorizedAllAgents(admin)
    }

    @AfterMethod
    fun cleanup() {
        testDataStateHelper.cleanCreatedProjects(admin)
        testDataStateHelper.unauthorizedAllAgents(admin)

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
        ProjectsApiAssistant.createProject(newProject, projectApi)
        // Create a new vcs root via API
        VcsRootApiAssistant.addNewVcsRoot(newVcsRoot, vcsRootApi)
        // Create a new build type via API
        BuildTypeApiAssistant.createBuildType(newBuildType, buildTypeApi)
        // Add a VCS root entry to the build type
        BuildTypeApiAssistant.addVcsRootEntryToBuild(newBuildType.id, newVscRootEntry, buildTypeApi)
        // Create a new step for the build via API
        BuildTypeApiAssistant.addStepToBuildType(newBuildType.id, newMvnStep, buildTypeApi)

        // Run build via UI
        buildFlow.runBuild(newProject.name, newBuildType.name)

        // Wait until the build is finished
        BuildApiAssistant.awaitUntilBuildFinished(newBuildType.id, buildApi)

        // Check that the build is succeeded
        BuildApiAssistant.getLatestBuild(newBuildType.id, buildApi).also {
            expectThat(it).isA<BuildDto>()
                .get { status }.isEqualTo("SUCCESS")
        }

        // Check the build content is correct
        BuildApiAssistant.getBuildStatistics(newBuildType.id, buildApi).also { props: PropertiesDto? ->
            expectThat(props).isA<PropertiesDto>()
                .get { property.find { it.name == "PassedTestCount" }?.value == "4" }
        }
    }

}