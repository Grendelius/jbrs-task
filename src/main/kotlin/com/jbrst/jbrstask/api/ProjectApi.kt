package com.jbrst.jbrstask.api

import com.jbrst.jbrstask.api.constants.ACCEPT_DEFAULT
import com.jbrst.jbrstask.api.constants.CONTENT_TYPE_DEFAULT
import com.jbrst.jbrstask.api.models.*
import com.jbrst.jbrstask.core.isOk
import io.qameta.allure.Step
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*
import strikt.api.expectThat

interface ProjectApi {

    @Headers(value = [ACCEPT_DEFAULT])
    @GET("/app/rest/projects/{id}")
    fun getProject(@Path("id") id: Locator): Call<ProjectDetailsDto>

    @Headers(value = [ACCEPT_DEFAULT])
    @GET("/app/rest/projects")
    fun listProjects(): Call<ProjectsDto>

    @Headers(value = [ACCEPT_DEFAULT, CONTENT_TYPE_DEFAULT])
    @POST("/app/rest/projects")
    fun addProject(@Body projectToCreate: NewProjectDescriptionDto): Call<ProjectDetailsDto>

    @Headers(value = [ACCEPT_DEFAULT])
    @DELETE("/app/rest/projects/{id}")
    fun deleteProject(@Path("id") id: Locator): Call<ProjectsDto>

}

object ProjectsApiAssistant {

    @Step("Create a project")
    fun createProject(projectDtoToCreate: NewProjectDescriptionDto, apiInstance: ProjectApi): ProjectDetailsDto? {
        return apiInstance.addProject(projectDtoToCreate).execute().also {
            expectThat(it).isOk()
        }.body()
    }

    @Step("Get a project")
    fun getProject(projectId: String, apiInstance: ProjectApi): ProjectDetailsDto? {
        return apiInstance.getProject(Id(value = projectId)).execute().also {
            expectThat(it).isOk()
        }.body()
    }

    @Step("Get a project")
    fun getProject_(projectId: String, apiInstance: ProjectApi): Response<ProjectDetailsDto> {
        return apiInstance.getProject(Id(value = projectId)).execute()
    }

}