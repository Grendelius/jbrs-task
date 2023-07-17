package com.jbrst.jbrstask.api

import com.jbrst.jbrstask.api.models.AgentDto
import com.jbrst.jbrstask.api.models.BuildDto
import com.jbrst.jbrstask.api.models.BuildTypeDto
import com.jbrst.jbrstask.api.models.NewProjectDescriptionDto
import io.qameta.allure.*
import io.qameta.allure.SeverityLevel.CRITICAL
import org.testng.annotations.AfterMethod
import org.testng.annotations.BeforeClass
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test
import strikt.api.expectThat
import strikt.assertions.*

@Story("As a user I want to be able to see and manage state of my build queues")
@Epic("Build queues")
@Features(value = [Feature("Build Queues Management")])
class BuildQueueApiTest : BaseApiTest() {

    private lateinit var testBuildType: BuildTypeDto
    companion object {
        private lateinit var projectApi: ProjectApi
        private lateinit var buildApi: BuildApi
        private lateinit var vcsRootApi: VcsRootApi
        private lateinit var buildTypeApi: BuildTypeApi
        private lateinit var buildQueueApi: BuildQueueApi
        private lateinit var agentApi: AgentApi
    }

    @BeforeClass
    fun initServicesAndTestUsers() {
        projectApi = apiServiceCreator.createService(ProjectApi::class.java, admin)
        buildApi = apiServiceCreator.createService(BuildApi::class.java, admin)
        vcsRootApi = apiServiceCreator.createService(VcsRootApi::class.java, admin)
        buildTypeApi = apiServiceCreator.createService(BuildTypeApi::class.java, admin)
        buildQueueApi = apiServiceCreator.createService(BuildQueueApi::class.java, admin)
        agentApi = apiServiceCreator.createService(AgentApi::class.java, admin)
    }

    @BeforeMethod
    fun createProject() {
        val projectName = testData.fakeProjectName()
        val newProject = NewProjectDescriptionDto(projectName.replaceFirstChar { it.titlecaseChar() }, projectName)
        val newBuildType = testData.buildTypeEntry(newProject.name)
        val newVcsRoot = testData.buildVcsRoot(newProject.id)
        val newVscRootEntry = testData.buildVcsRootEntry(newVcsRoot)
        val newMvnStep = testData.buildTestMavenStep()

        ProjectsApiAssistant.createProject(newProject, projectApi)
        VcsRootApiAssistant.addNewVcsRoot(newVcsRoot, vcsRootApi)
        BuildTypeApiAssistant.createBuildType(newBuildType, buildTypeApi)
        BuildTypeApiAssistant.addVcsRootEntryToBuild(newBuildType.id, newVscRootEntry, buildTypeApi)
        BuildTypeApiAssistant.addStepToBuildType(newBuildType.id, newMvnStep, buildTypeApi)

        testBuildType = newBuildType
    }

    @Test
    @Severity(CRITICAL)
    fun userIsAbleToSeeBuildInQueueIfThereIsNoAuthorizedAgentsTest() {
        // By Default All connected Agents are unauthorized
        AgentApiAssistant.getEnabledAuthorizedAgents(agentApi).also {
            expectThat(it?.agent)
                .isA<List<AgentDto>>()
                .isEmpty()
        }

        val build = BuildDto(buildType = testBuildType)

        // Start the build via API
        BuildQueueApiAssistant.startBuild(build, buildQueueApi)

        //Checkin that the build in the queue
        BuildQueueApiAssistant.getQueuedBuildsByBuildTypeId(testBuildType.id, buildQueueApi).also {
            expectThat(it?.build)
                .isA<List<BuildDto>>()
                .filter { build: BuildDto -> build.buildTypeId == testBuildType.id && build.state == "queued"}
                .isNotEmpty()
        }
    }

}