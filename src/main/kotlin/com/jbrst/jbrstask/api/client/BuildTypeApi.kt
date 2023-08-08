package com.jbrst.jbrstask.api.client

import com.jbrst.jbrstask.api.constants.ACCEPT_DEFAULT
import com.jbrst.jbrstask.api.constants.CONTENT_TYPE_DEFAULT
import com.jbrst.jbrstask.api.models.*
import retrofit2.Call
import retrofit2.http.*

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