package com.jbrst.jbrstask.api.assistants

import com.jbrst.jbrstask.api.client.ProjectApi
import com.jbrst.jbrstask.api.models.Id
import com.jbrst.jbrstask.api.models.NewProjectDescriptionDto
import com.jbrst.jbrstask.api.models.ProjectDetailsDto
import com.jbrst.jbrstask.core.isOk
import io.qameta.allure.Step
import org.springframework.stereotype.Component
import retrofit2.Response
import strikt.api.expectThat

@Component
class ProjectApiAssistant(private val projectApi: ProjectApi) {

    @Step("Create a project")
    fun createProject(projectDtoToCreate: NewProjectDescriptionDto): ProjectDetailsDto? {
        return projectApi.addProject(projectDtoToCreate).execute().also {
            expectThat(it).isOk()
        }.body()
    }

    @Step("Get a project")
    fun getProject(projectId: String): ProjectDetailsDto? {
        return projectApi.getProject(Id(value = projectId)).execute().also {
            expectThat(it).isOk()
        }.body()
    }

    @Step("Get a project")
    fun getProject_(projectId: String): Response<ProjectDetailsDto> {
        return projectApi.getProject(Id(value = projectId)).execute()
    }

}