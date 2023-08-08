package com.jbrst.jbrstask.api.client

import com.jbrst.jbrstask.api.constants.ACCEPT_DEFAULT
import com.jbrst.jbrstask.api.constants.ACCEPT_TYPE_TEXT
import com.jbrst.jbrstask.api.constants.CONTENT_TYPE_TEXT
import com.jbrst.jbrstask.api.models.AgentsDto
import com.jbrst.jbrstask.api.models.Locator
import com.jbrst.jbrstask.api.models.Locators
import retrofit2.Call
import retrofit2.http.*

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