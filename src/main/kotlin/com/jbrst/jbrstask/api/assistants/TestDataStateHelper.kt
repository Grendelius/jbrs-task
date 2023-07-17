package com.jbrst.jbrstask.api.assistants

import com.jbrst.jbrstask.api.AgentApi
import com.jbrst.jbrstask.api.ProjectApi
import com.jbrst.jbrstask.api.UsersApi
import com.jbrst.jbrstask.api.models.*
import com.jbrst.jbrstask.core.isOk
import com.jbrst.jbrstask.core.models.User
import io.qameta.allure.Step
import org.springframework.stereotype.Component
import retrofit2.Response
import strikt.api.expectThat
import strikt.assertions.isTrue


@Component
class TestDataStateHelper(private val apiServiceCreator: ApiServiceCreator) {

    @Step("Clean up the created projects")
    fun cleanCreatedProjects(invoker: User) {
        val api = apiServiceCreator.createService(ProjectApi::class.java, invoker)

        val existingProjects = api.listProjects()

        existingProjects.execute().body()?.project
            ?.asSequence()
            ?.filter { it.id != "_Root" }
            ?.map { Id(value = it.id) }
            ?.map { id -> api.deleteProject(id).execute() }
            ?.forEach { response: Response<ProjectsDto> -> expectThat(response) { isOk() } }
    }

    @Step("Clean up the created user accounts")
    fun cleanExtraUsers(invoker: User) {
        val api = apiServiceCreator.createService(UsersApi::class.java, invoker)

        val extraUsers = api.listUsers()

        extraUsers.execute().body()?.user
            ?.asSequence()
            ?.filter { user -> user.username != "admin" }
            ?.map { api.removeUser(Id(value = it.id.toString())).execute() }
            ?.forEach { response: Response<String> -> expectThat(response) { isOk() } }
    }

    @Step("Clean up the created projects")
    fun unauthorizedAllAgents(invoker: User) {
        val api = apiServiceCreator.createService(AgentApi::class.java, invoker)
        val locators = Locators(Connected(value = "true"), Authorized(value = "true"))
        val authorizedAgents = api.getAgents(locators)

        authorizedAgents.execute().body()?.agent
            ?.asSequence()
            ?.map { api.authorizeUnauthorizeAgent(Id(value = it.id.toString()), false).execute() }
            ?.forEach { response: Response<Boolean> -> expectThat(response) { isOk() } }
    }

    @Step("Authorize all available agents")
    fun authorizedAllAgents(invoker: User) {
        val api = apiServiceCreator.createService(AgentApi::class.java, invoker)
        val locators = Locators(Connected(value = "true"), Authorized(value = "false"))
        val authorizedAgents = api.getAgents(locators)

        authorizedAgents.execute().body()?.agent
            ?.asSequence()
            ?.map { api.authorizeUnauthorizeAgent(Id(value = it.id.toString()), true).execute() }
            ?.forEach { response: Response<Boolean> -> expectThat(response) { isOk() } }
    }

    @Step("Enable all available agents")
    fun enableAllAgents(invoker: User) {
        val api = apiServiceCreator.createService(AgentApi::class.java, invoker)
        val locators = Locators(Connected(value = "true"), Enabled(value = "false"))
        val authorizedAgents = api.getAgents(locators)

        authorizedAgents.execute().body()?.agent
            ?.asSequence()
            ?.map { api.enableDisableAgent(Id(value = it.id.toString()), true).execute() }
            ?.forEach { response: Response<Boolean> -> expectThat(response) { isOk().and { get { body() }.isTrue() } } }
    }

}

