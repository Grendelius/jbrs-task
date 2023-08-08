package com.jbrst.jbrstask.api.assistants

import com.jbrst.jbrstask.api.client.BuildApi
import com.jbrst.jbrstask.api.models.*
import com.jbrst.jbrstask.core.isOk
import io.qameta.allure.Step
import org.awaitility.kotlin.*
import org.springframework.stereotype.Component
import strikt.api.expectThat
import java.time.Duration

@Component
class BuildApiAssistant(private val buildApi: BuildApi) {

    private val aHalfOfMinute = Duration.ofSeconds(30)
    private val everyTwoSeconds = Duration.ofSeconds(2)

    @Step("Get Builds")
    fun getBuilds(buildConfigId: String): BuildsDto? {
        val locator = BuildQueueLocators(BuildType(locator = Id(value = buildConfigId)))
        return buildApi.getRecentBuilds(locator).execute().also {
            expectThat(it).isOk()
        }.body()
    }

    @Step("Get Latest Build")
    fun getLatestBuild(buildConfigId: String): BuildDto? {
        await atMost aHalfOfMinute withPollInterval everyTwoSeconds until {
            getBuilds(buildConfigId)?.count != 0
        }
        return getBuilds(buildConfigId)?.build?.maxByOrNull { it.number.toString() }
    }

    @Step("Get Build Statistics")
    fun getBuildStatistics(buildConfigId: String): PropertiesDto? {
        val locator = BuildQueueLocators(BuildType(locator = Id(value = buildConfigId)))
        return buildApi.getBuildStatisticsValues(locator).execute().also {
            expectThat(it).isOk()
        }.body()
    }

    @Step("Wait until build finished")
    fun awaitUntilBuildFinished(buildConfigId: String) {
        await atMost aHalfOfMinute withPollInterval everyTwoSeconds untilCallTo {
            getLatestBuild(buildConfigId)
        } has {
            state == "finished"
        }
    }

}