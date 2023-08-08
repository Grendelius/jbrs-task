package com.jbrst.jbrstask.api.assistants

import com.jbrst.jbrstask.api.client.AgentApi
import com.jbrst.jbrstask.api.models.*
import com.jbrst.jbrstask.core.isOk
import io.qameta.allure.Step
import org.springframework.stereotype.Component
import strikt.api.expectThat

@Component
class AgentApiAssistant(private val agentApi: AgentApi) {

    @Step("Get Agents List")
    private fun getAgents(locators: Locators): AgentsDto? {
        return agentApi.getAgents(locators).execute().also {
            expectThat(it).isOk()
        }.body()
    }

    @Step("Enable Agent")
    fun enableAgent(agentId: String): Boolean? =
        agentApi.enableDisableAgent(Id(value = agentId), true).execute().body()

    @Step("Disable Agent")
    fun disableAgent(agentId: String): Boolean? =
        agentApi.enableDisableAgent(Id(value = agentId), false).execute().body()

    @Step("Authorize Agent")
    fun authorizeAgent(agentId: String): Boolean? =
        agentApi.authorizeUnauthorizeAgent(Id(value = agentId), true).execute().body()

    @Step("Unauthorize Agent")
    fun unauthorizeAgent(agentId: String): Boolean? =
        agentApi.authorizeUnauthorizeAgent(Id(value = agentId), false).execute().body()

    @Step("Get Enabled Unauthorized Agents")
    fun getEnabledUnauthorizedAgents(): AgentsDto? {
        return getAgents(
            Locators(Connected(value = "true"), Authorized(value = "false"), Enabled(value = "true"))
        )
    }

    @Step("Get Enabled Authorized Agents")
    fun getEnabledAuthorizedAgents(): AgentsDto? {
        return getAgents(
            Locators(Connected(value = "true"), Authorized(value = "true"), Enabled(value = "true"))
        )
    }

    @Step("Get Disabled Unauthorized Agents")
    fun getDisabledUnauthorizedAgents(): AgentsDto? {
        return getAgents(
            Locators(Connected(value = "true"), Authorized(value = "false"), Enabled(value = "false"))
        )
    }

    @Step("Enable Disabled Authorized Agents")
    fun getDisabledAuthorizedAgents(): AgentsDto? {
        return getAgents(
            Locators(Connected(value = "true"), Authorized(value = "true"), Enabled(value = "false"))
        )
    }

}