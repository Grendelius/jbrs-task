package com.jbrst.jbrstask.ui

import com.jbrst.jbrstask.api.ProjectApi
import com.jbrst.jbrstask.api.ProjectsApiAssistant
import com.jbrst.jbrstask.api.models.NewProjectDescriptionDto
import com.jbrst.jbrstask.api.models.ProjectDetailsDto
import com.jbrst.jbrstask.core.annotations.DesktopTest
import com.jbrst.jbrstask.core.fullUrl
import com.jbrst.jbrstask.core.isNotFound
import com.jbrst.jbrstask.ui.flows.DiscoverRunnersPage
import com.jbrst.jbrstask.ui.flows.EditBuildRunnersPage
import io.qameta.allure.*
import io.qameta.allure.SeverityLevel.CRITICAL
import org.testng.annotations.AfterMethod
import org.testng.annotations.BeforeClass
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test
import strikt.api.expectThat
import strikt.assertions.isA
import strikt.assertions.isEqualTo

@Story("As a user I want to be able to manage my projects")
@Epic("Projects")
@Features(value = [Feature("Project Management")])
class ProjectsUiTest : BaseUiTest() {

    companion object {
        private lateinit var projectApi: ProjectApi
    }

    @BeforeClass
    fun initServicesAndTestUsers() {
        projectApi = apiServiceCreator.createService(ProjectApi::class.java, admin)
    }

    @BeforeMethod
    fun login() {
        loginFlow.loggedAs(admin)
    }

    @AfterMethod
    fun cleanup() {
        testDataStateHelper.cleanCreatedProjects(admin)
    }

    @Test
    @Severity(CRITICAL)
    fun userIsAbleToCreateProjectFromVcsUrlTest() {
        val vcsRepoEntry = testData.gitRepoProjectEntry()
        val buildConfigName = testData.fakeBuildName()
        val projectName = testData.fakeProjectName()
        val projectId = projectName.replaceFirstChar { it.titlecaseChar() }

        val connectionVerifiedMsg = "The connection to the VCS repository has been verified"
        val newProjectIsCreatedMsg =
            """New project "$projectName", build configuration "$buildConfigName" and VCS root 
                "${vcsRepoEntry.vcsRepo.fullUrl()}#refs/heads/main" have been successfully created.
            """.trimIndent()
        val stepAddedMsg = "New build step added."

        // Creating of a new project from the Project with build name configuration info using Git VCS configuration
        val autoDetectedStepIndex = 0

        projectsFlow
            .createProjectFromVcsUrl(vcsRepoEntry)
            .successMessageIsDisplayed(connectionVerifiedMsg)
            .inputProjectName(projectName)
            .inputBuildConfigName(buildConfigName)
            .proceed()
            .on<DiscoverRunnersPage>()
            .successMessageIsDisplayed(newProjectIsCreatedMsg)
            .waitForVcsProgressingLoader()
            .chooseAutoDetectedStep(autoDetectedStepIndex)
            .useSelectedSteps()
            .on<EditBuildRunnersPage>()
            .successMessageIsDisplayed(stepAddedMsg)

        // Checking that the project is exising via API
        ProjectsApiAssistant.getProject(projectId, projectApi).also {
            expectThat(it).isA<ProjectDetailsDto>()
                .and {
                    get { projectName }.isEqualTo(projectName)
                    get { projectId }.isEqualTo(projectId)
                }
        }
    }

    @Test
    @Severity(CRITICAL)
    @DesktopTest
    fun userIsAbleToDeleteProjectTest() {
        val projectName = testData.fakeProjectName()
        val projectId = projectName.uppercase()

        val newProject = NewProjectDescriptionDto(projectId, projectName)

        val deletedMsg = """
            Project "$projectName" has been moved to the "config/_trash" directory. 
            All project related data (build history, artifacts, and so on) will be cleaned from the database during the next clean-up. 
            See clean-up policy configuration.
        """.trimIndent()

        // Create a new project via API
        ProjectsApiAssistant.createProject(newProject, projectApi)

        // Delete the project via UI
        projectsFlow
            .deleteProject(projectId)
            .projectDeletedMessageIsDisplayed(deletedMsg)

        // Check project is deleted via API
        val response = ProjectsApiAssistant.getProject_(projectId, projectApi)

        expectThat(response).isNotFound()
    }

}