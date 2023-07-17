package com.jbrst.jbrstask.api

import com.jbrst.jbrstask.api.constants.ACCEPT_DEFAULT
import com.jbrst.jbrstask.api.constants.ACCEPT_TYPE_TEXT
import com.jbrst.jbrstask.api.constants.CONTENT_TYPE_TEXT
import com.jbrst.jbrstask.api.models.*
import com.jbrst.jbrstask.core.isOk
import io.qameta.allure.Step
import retrofit2.Call
import retrofit2.http.*
import strikt.api.expectThat

interface AgentApi {

    @Headers(value = [ACCEPT_DEFAULT])
    @GET("/app/rest/agents")
    fun getAgents(@Query("locator") locators: Locators): Call<AgentsDto>

    @Headers(value = [ACCEPT_TYPE_TEXT, CONTENT_TYPE_TEXT])
    @PUT("/app/rest/agents/{id}/enabled")
    fun enableDisableAgent(@Path("id") id: Locator, @Body status: Boolean): Call<Boolean>

    @Headers(value = [ACCEPT_TYPE_TEXT, CONTENT_TYPE_TEXT])
    @PUT("/app/rest/agents/{id}/authorized")
    fun authorizeUnauthorizeAgent(@Path("id") id: Locator, @Body status: Boolean): Call<Boolean>

}

object AgentApiAssistant {

    @Step("Get Agents List")
    private fun getAgents(apiInstance: AgentApi, locators: Locators): AgentsDto? {
        return apiInstance.getAgents(locators).execute().also {
            expectThat(it).isOk()
        }.body()
    }

    @Step("Enable Agent")
    fun enableAgent(agentId: String, apiInstance: AgentApi): Boolean? =
        apiInstance.enableDisableAgent(Id(value = agentId), true).execute().body()

    @Step("Disable Agent")
    fun disableAgent(agentId: String, apiInstance: AgentApi): Boolean? =
        apiInstance.enableDisableAgent(Id(value = agentId), false).execute().body()

    @Step("Authorize Agent")
    fun authorizeAgent(agentId: String, apiInstance: AgentApi): Boolean? =
        apiInstance.authorizeUnauthorizeAgent(Id(value = agentId), true).execute().body()

    @Step("Unauthorize Agent")
    fun unauthorizeAgent(agentId: String, apiInstance: AgentApi): Boolean? =
        apiInstance.authorizeUnauthorizeAgent(Id(value = agentId), false).execute().body()

    @Step("Get Enabled Unauthorized Agents")
    fun getEnabledUnauthorizedAgents(apiInstance: AgentApi): AgentsDto? {
        return getAgents(
            apiInstance,
            Locators(Connected(value = "true"), Authorized(value = "false"), Enabled(value = "true"))
        )
    }

    @Step("Get Enabled Authorized Agents")
    fun getEnabledAuthorizedAgents(apiInstance: AgentApi): AgentsDto? {
        return getAgents(
            apiInstance,
            Locators(Connected(value = "true"), Authorized(value = "true"), Enabled(value = "true"))
        )
    }

    @Step("Get Disabled Unauthorized Agents")
    fun getDisabledUnauthorizedAgents(apiInstance: AgentApi): AgentsDto? {
        return getAgents(
            apiInstance,
            Locators(Connected(value = "true"), Authorized(value = "false"), Enabled(value = "false"))
        )
    }

    @Step("Enable Disabled Authorized Agents")
    fun getDisabledAuthorizedAgents(apiInstance: AgentApi): AgentsDto? {
        return getAgents(
            apiInstance,
            Locators(Connected(value = "true"), Authorized(value = "true"), Enabled(value = "false"))
        )
    }
}