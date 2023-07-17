package com.jbrst.jbrstask.api

import com.jbrst.jbrstask.api.models.*
import io.qameta.allure.*
import io.qameta.allure.SeverityLevel.CRITICAL
import org.testng.annotations.AfterMethod
import org.testng.annotations.BeforeClass
import org.testng.annotations.Test
import strikt.api.expectThat
import strikt.assertions.*

@Story("As a user I want to be able to manage agent machines on a server")
@Epic("Agent")
@Feature("Agent Management")
class AgentApiTest : BaseApiTest() {

    companion object {
        private lateinit var agentApi: AgentApi
    }

    @BeforeClass
    fun initServicesAndTestData() {
        agentApi = apiServiceCreator.createService(AgentApi::class.java, admin)
    }

    @AfterMethod
    fun rollbackAgentsState() {
        testDataStateHelper.unauthorizedAllAgents(admin)
        testDataStateHelper.enableAllAgents(admin)
    }

    @Test
    @Severity(CRITICAL)
    fun userIsAbleToAuthorizeConnectedAgentToServerTest() {
        // Get any unauthorized agent
        val unauthorizedAgentId =
            AgentApiAssistant.getEnabledUnauthorizedAgents(agentApi)?.agent?.first()?.id.toString()

        // Authorize a first unauthorized agent
        val status = AgentApiAssistant.authorizeAgent(unauthorizedAgentId, agentApi)

        // Check that the agent has been authorized
        expectThat(status) {
            isA<Boolean>()
                .isTrue()
        }

        val authorizedAgents = AgentApiAssistant.getEnabledAuthorizedAgents(agentApi)

        expectThat(authorizedAgents?.agent).isA<List<AgentDto>>()
            .any { with(AgentDto::id) { isEqualTo(unauthorizedAgentId.toInt()) } }
    }

    @Test
    @Severity(CRITICAL)
    fun userIsAbleToDisableAndEnableUnauthorizedAgentTest() {
        // Get any unauthorized agent
        val unauthorizedAgentId =
            AgentApiAssistant.getEnabledUnauthorizedAgents(agentApi)?.agent?.first()?.id.toString()

        // Disable the agent
        var status = AgentApiAssistant.disableAgent(unauthorizedAgentId, agentApi)

        // Check that the agent has been disabled
        expectThat(status) {
            isA<Boolean>()
                .isFalse()
        }

        val disabledAgents = AgentApiAssistant.getDisabledUnauthorizedAgents(agentApi)

        expectThat(disabledAgents?.agent).isA<List<AgentDto>>()
            .any { with(AgentDto::id) { isEqualTo(unauthorizedAgentId.toInt()) } }

        // Rollback. Enable the agent
        status = AgentApiAssistant.enableAgent(unauthorizedAgentId, agentApi)

        // Check that the agent has been enabled
        expectThat(status) {
            isA<Boolean>()
                .isTrue()
        }

        val enabledAgents = AgentApiAssistant.getEnabledUnauthorizedAgents(agentApi)

        expectThat(enabledAgents?.agent).isA<List<AgentDto>>()
            .any { with(AgentDto::id) { isEqualTo(unauthorizedAgentId.toInt()) } }
    }

}