package com.jbrst.jbrstask.api

import com.jbrst.jbrstask.api.constants.ACCEPT_DEFAULT
import com.jbrst.jbrstask.api.constants.CONTENT_TYPE_DEFAULT
import com.jbrst.jbrstask.api.models.*
import com.jbrst.jbrstask.core.isOk
import io.qameta.allure.Step
import retrofit2.Call
import retrofit2.http.*
import strikt.api.expectThat

interface BuildTypeApi {

    @Headers(value = [ACCEPT_DEFAULT])
    @GET("/app/rest/buildTypes/{id}")
    fun listBuildConfigs(@Path("id") buildTypeConfId: Id): Call<BuildTypesDto>

    @Headers(value = [ACCEPT_DEFAULT, CONTENT_TYPE_DEFAULT])
    @POST("/app/rest/buildTypes")
    fun addBuildType(@Body buildTypeToCreate: BuildTypeDto): Call<BuildTypeDto>

    @Headers(value = [ACCEPT_DEFAULT, CONTENT_TYPE_DEFAULT])
    @POST("/app/rest/buildTypes/{btLocator}/steps")
    fun addBuildStepToBuildType(@Path("btLocator") locator: Locator, @Body step: StepDto): Call<StepDto>

    @Headers(value = [ACCEPT_DEFAULT, CONTENT_TYPE_DEFAULT])
    @POST("/app/rest/buildTypes/{btLocator}/vcs-root-entries")
    fun addVcsRootEntry(@Path("btLocator") locators: Locators, @Body vcsRoot: VcsRootEntry): Call<VcsRootEntry>

}

object BuildTypeApiAssistant {

    @Step("List Build Types")
    fun getBuildType(buildTypeId: String, apiInstance: BuildTypeApi): BuildTypesDto? {
        val locator = Id(value = buildTypeId)
        return apiInstance.listBuildConfigs(locator).execute().also {
            expectThat(it).isOk()
        }.body()
    }

    @Step("Create Build Type")
    fun createBuildType(buildTypeDto: BuildTypeDto, apiInstance: BuildTypeApi): BuildTypeDto? {
        return apiInstance.addBuildType(buildTypeDto).execute().also {
            expectThat(it).isOk()
        }.body()
    }

    @Step("Add Vcs Root Entry to Build")
    fun addVcsRootEntryToBuild(
        buildConfigId: String,
        vcsRootEntry: VcsRootEntry,
        apiInstance: BuildTypeApi
    ): VcsRootEntry? {
        val locator = Locators(Id(value = buildConfigId))
        return apiInstance.addVcsRootEntry(locator, vcsRootEntry).execute().also {
            expectThat(it).isOk()
        }.body()
    }

    @Step("Add Step to Build Type")
    fun addStepToBuildType(buildStepId: String, step: StepDto, apiInstance: BuildTypeApi): StepDto? {
        val locator = Id(value = buildStepId)
        return apiInstance.addBuildStepToBuildType(locator, step).execute().also {
            expectThat(it).isOk()
        }.body()
    }

}