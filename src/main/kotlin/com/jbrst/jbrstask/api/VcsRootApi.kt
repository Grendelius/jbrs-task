package com.jbrst.jbrstask.api

import com.jbrst.jbrstask.api.constants.ACCEPT_DEFAULT
import com.jbrst.jbrstask.api.constants.CONTENT_TYPE_DEFAULT
import com.jbrst.jbrstask.api.models.VcsRootDto
import com.jbrst.jbrstask.api.models.VcsRootsDto
import com.jbrst.jbrstask.core.isOk
import io.qameta.allure.Step
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import strikt.api.expectThat

interface VcsRootApi {

    @Headers(value = [ACCEPT_DEFAULT, CONTENT_TYPE_DEFAULT])
    @POST("/app/rest/vcs-roots")
    fun addVcsRoot(@Body vcsRootToCreate: VcsRootDto): Call<VcsRootDto>

    @Headers(value = [ACCEPT_DEFAULT, CONTENT_TYPE_DEFAULT])
    @GET("/app/rest/vcs-roots")
    fun listVcsRoots(): Call<VcsRootsDto>

}

object VcsRootApiAssistant {

    @Step("Add a new Vcs Root")
    fun addNewVcsRoot(vcsRootToCreate: VcsRootDto, apiInstance: VcsRootApi): VcsRootDto? {
        return apiInstance.addVcsRoot(vcsRootToCreate).execute().also {
            expectThat(it).isOk()
        }.body()
    }

}