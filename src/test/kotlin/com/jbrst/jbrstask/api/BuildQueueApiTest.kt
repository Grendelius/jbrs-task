package com.jbrst.jbrstask.api

import com.jbrst.jbrstask.api.models.AgentDto
import com.jbrst.jbrstask.api.models.BuildDto
import com.jbrst.jbrstask.api.models.BuildTypeDto
import com.jbrst.jbrstask.api.models.NewProjectDescriptionDto
import io.qameta.allure.*
import io.qameta.allure.SeverityLevel.CRITICAL
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test
import strikt.api.expectThat
import strikt.assertions.filter
import strikt.assertions.isA
import strikt.assertions.isEmpty
import strikt.assertions.isNotEmpty

@Story("As a user I want to be able to see and manage state of my build queues")
@Epic("Build queues")
@Features(value = [Feature("Build Queues Management")])
class BuildQueueApiTest : BaseApiTest() {

    private lateinit var testBuildType: BuildTypeDto

    @BeforeMethod
    fun createProject() {
        val projectName = testData.fakeProjectName()
        val newProject = NewProjectDescriptionDto(projectName.replaceFirstChar { it.titlecaseChar() }, projectName)
        val newBuildType = testData.buildTypeEntry(newProject.name)
        val newVcsRoot = testData.buildVcsRoot(newProject.id)
        val newVscRootEntry = testData.buildVcsRootEntry(newVcsRoot)
        val newMvnStep = testData.buildTestMavenStep()

        projectAssistant.createProject(newProject)
        vcsRootAssistant.addNewVcsRoot(newVcsRoot)
        buildTypeAssistant.createBuildType(newBuildType)
        buildTypeAssistant.addVcsRootEntryToBuild(newBuildType.id, newVscRootEntry)
        buildTypeAssistant.addStepToBuildType(newBuildType.id, newMvnStep)

        testBuildType = newBuildType
    }

    @Test
    @Severity(CRITICAL)
    fun userIsAbleToSeeBuildInQueueIfThereIsNoAuthorizedAgentsTest() {
        // By Default All connected Agents are unauthorized
        agentAssistant.getEnabledAuthorizedAgents().also {
            expectThat(it?.agent)
                .isA<List<AgentDto>>()
                .isEmpty()
        }

        val build = BuildDto(buildType = testBuildType)

        // Start the build via API
        buildQueueAssistant.startBuild(build)

        //Checking that the build is in the queue
        buildQueueAssistant.getQueuedBuildsByBuildTypeId(testBuildType.id).also {
            expectThat(it?.build)
                .isA<List<BuildDto>>()
                .filter { build: BuildDto -> build.buildTypeId == testBuildType.id && build.state == "queued" }
                .isNotEmpty()
        }
    }

}