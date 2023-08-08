package com.jbrst.jbrstask.api.client

import com.jbrst.jbrstask.api.constants.ACCEPT_DEFAULT
import com.jbrst.jbrstask.api.constants.CONTENT_TYPE_DEFAULT
import com.jbrst.jbrstask.api.models.Locator
import com.jbrst.jbrstask.api.models.NewProjectDescriptionDto
import com.jbrst.jbrstask.api.models.ProjectDetailsDto
import com.jbrst.jbrstask.api.models.ProjectsDto
import retrofit2.Call
import retrofit2.http.*

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