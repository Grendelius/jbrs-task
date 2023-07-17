package com.jbrst.jbrstask.api

import com.jbrst.jbrstask.api.constants.ACCEPT_DEFAULT
import com.jbrst.jbrstask.api.constants.CONTENT_TYPE_DEFAULT
import com.jbrst.jbrstask.api.models.*
import com.jbrst.jbrstask.core.isOk
import io.qameta.allure.Step
import retrofit2.Call
import retrofit2.http.*
import strikt.api.expectThat


interface BuildQueueApi {

    @Headers(value = [ACCEPT_DEFAULT, CONTENT_TYPE_DEFAULT])
    @POST("/app/rest/buildQueue")
    fun addBuildToQueue(@Body buildTypeToCreate: BuildDto): Call<BuildDto>

    @Headers(value = [ACCEPT_DEFAULT])
    @GET("/app/rest/buildQueue")
    fun getQueuedBuilds(@Query("locator") locator: BuildQueueLocator): Call<BuildsDto>

}

object BuildQueueApiAssistant {

    @Step("Get Queued Builds by Type Id")
    fun getQueuedBuildsByBuildTypeId(buildTypeId: String, apiInstance: BuildQueueApi): BuildsDto? {
        val locator = BuildType(locator = Id(value = buildTypeId))
        return apiInstance.getQueuedBuilds(locator).execute().also {
            expectThat(it).isOk()
        }.body()
    }

    @Step("Start Build")
    fun startBuild(build: BuildDto, apiInstance: BuildQueueApi): BuildDto? {
        return apiInstance.addBuildToQueue(build).execute().also {
            expectThat(it).isOk()
        }.body()
    }

}
