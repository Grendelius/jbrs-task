package com.jbrst.jbrstask.api.assistants

import com.jbrst.jbrstask.api.client.BuildQueueApi
import com.jbrst.jbrstask.api.models.BuildDto
import com.jbrst.jbrstask.api.models.BuildType
import com.jbrst.jbrstask.api.models.BuildsDto
import com.jbrst.jbrstask.api.models.Id
import com.jbrst.jbrstask.core.isOk
import io.qameta.allure.Step
import org.springframework.stereotype.Component
import strikt.api.expectThat

@Component
class BuildQueueApiAssistant(private val buildQueueApi: BuildQueueApi) {

    @Step("Get Queued Builds by Type Id")
    fun getQueuedBuildsByBuildTypeId(buildTypeId: String): BuildsDto? {
        val locator = BuildType(locator = Id(value = buildTypeId))
        return buildQueueApi.getQueuedBuilds(locator).execute().also {
            expectThat(it).isOk()
        }.body()
    }

    @Step("Start Build")
    fun startBuild(build: BuildDto): BuildDto? {
        return buildQueueApi.addBuildToQueue(build).execute().also {
            expectThat(it).isOk()
        }.body()
    }

}