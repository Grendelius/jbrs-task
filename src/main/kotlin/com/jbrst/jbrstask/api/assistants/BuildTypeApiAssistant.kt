package com.jbrst.jbrstask.api.assistants

import com.jbrst.jbrstask.api.client.BuildTypeApi
import com.jbrst.jbrstask.api.models.*
import com.jbrst.jbrstask.core.isOk
import io.qameta.allure.Step
import org.springframework.stereotype.Component
import strikt.api.expectThat

@Component
class BuildTypeApiAssistant(private val buildTypeApi: BuildTypeApi) {

    @Step("List Build Types")
    fun getBuildType(buildTypeId: String): BuildTypesDto? {
        val locator = Id(value = buildTypeId)
        return buildTypeApi.listBuildConfigs(locator).execute().also {
            expectThat(it).isOk()
        }.body()
    }

    @Step("Create Build Type")
    fun createBuildType(buildTypeDto: BuildTypeDto): BuildTypeDto? {
        return buildTypeApi.addBuildType(buildTypeDto).execute().also {
            expectThat(it).isOk()
        }.body()
    }

    @Step("Add Vcs Root Entry to Build")
    fun addVcsRootEntryToBuild(
        buildConfigId: String,
        vcsRootEntry: VcsRootEntry
    ): VcsRootEntry? {
        val locator = Locators(Id(value = buildConfigId))
        return buildTypeApi.addVcsRootEntry(locator, vcsRootEntry).execute().also {
            expectThat(it).isOk()
        }.body()
    }

    @Step("Add Step to Build Type")
    fun addStepToBuildType(buildStepId: String, step: StepDto): StepDto? {
        val locator = Id(value = buildStepId)
        return buildTypeApi.addBuildStepToBuildType(locator, step).execute().also {
            expectThat(it).isOk()
        }.body()
    }

}