package com.jbrst.jbrstask.api

import com.jbrst.jbrstask.api.models.AgentDto
import io.qameta.allure.Epic
import io.qameta.allure.Feature
import io.qameta.allure.Severity
import io.qameta.allure.SeverityLevel.CRITICAL
import io.qameta.allure.Story
import org.testng.annotations.AfterMethod
import org.testng.annotations.Test
import strikt.api.expectThat
import strikt.assertions.*

@Story("As a user I want to be able to manage agent machines on a server")
@Epic("Agent")
@Feature("Agent Management")
class AgentApiTest : BaseApiTest() {

    @AfterMethod
    fun rollbackAgentsState() {
        testDataStateHelper.unauthorizedAllAgents()
        testDataStateHelper.enableAllAgents()
    }

    @Test
    @Severity(CRITICAL)
    fun userIsAbleToAuthorizeConnectedAgentToServerTest() {
        // Get any unauthorized agent
        val unauthorizedAgentId = agentAssistant.getEnabledUnauthorizedAgents()?.agent?.first()?.id.toString()

        // Authorize a first unauthorized agent
        val status = agentAssistant.authorizeAgent(unauthorizedAgentId)

        // Check that the agent has been authorized
        expectThat(status) {
            isA<Boolean>()
                .isTrue()
        }

        val authorizedAgents = agentAssistant.getEnabledAuthorizedAgents()

        expectThat(authorizedAgents?.agent).isA<List<AgentDto>>()
            .any { with(AgentDto::id) { isEqualTo(unauthorizedAgentId.toInt()) } }
    }

    @Test
    @Severity(CRITICAL)
    fun userIsAbleToDisableAndEnableUnauthorizedAgentTest() {
        // Get any unauthorized agent
        val unauthorizedAgentId =
            agentAssistant.getEnabledUnauthorizedAgents()?.agent?.first()?.id.toString()

        // Disable the agent
        var status = agentAssistant.disableAgent(unauthorizedAgentId)

        // Check that the agent has been disabled
        expectThat(status) {
            isA<Boolean>()
                .isFalse()
        }

        val disabledAgents = agentAssistant.getDisabledUnauthorizedAgents()

        expectThat(disabledAgents?.agent).isA<List<AgentDto>>()
            .any { with(AgentDto::id) { isEqualTo(unauthorizedAgentId.toInt()) } }

        // Rollback. Enable the agent
        status = agentAssistant.enableAgent(unauthorizedAgentId)

        // Check that the agent has been enabled
        expectThat(status) {
            isA<Boolean>()
                .isTrue()
        }

        val enabledAgents = agentAssistant.getEnabledUnauthorizedAgents()

        expectThat(enabledAgents?.agent).isA<List<AgentDto>>()
            .any { with(AgentDto::id) { isEqualTo(unauthorizedAgentId.toInt()) } }
    }

}