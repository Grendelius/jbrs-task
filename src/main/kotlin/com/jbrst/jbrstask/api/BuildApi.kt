package com.jbrst.jbrstask.api

import com.jbrst.jbrstask.api.constants.ACCEPT_DEFAULT
import com.jbrst.jbrstask.api.models.*
import com.jbrst.jbrstask.core.isOk
import io.qameta.allure.Step
import org.awaitility.kotlin.*
import retrofit2.Call
import retrofit2.http.*
import strikt.api.expectThat
import java.time.Duration

interface BuildApi {

    @Headers(value = [ACCEPT_DEFAULT])
    @GET("/app/rest/builds")
    fun getRecentBuilds(@Query("locator") locators: BuildQueueLocators): Call<BuildsDto>

    @Headers(value = [ACCEPT_DEFAULT])
    @GET("/app/rest/builds/{btLocator}/statistics")
    fun getBuildStatisticsValues(@Path("btLocator") locators: BuildQueueLocators): Call<PropertiesDto>

}

object BuildApiAssistant {

    private val aHalfOfMinute = Duration.ofSeconds(30)
    private val everyTwoSeconds = Duration.ofSeconds(2)

    @Step("Get Builds")
    fun getBuilds(buildConfigId: String, apiInstance: BuildApi): BuildsDto? {
        val locator = BuildQueueLocators(BuildType(locator = Id(value = buildConfigId)))
        return apiInstance.getRecentBuilds(locator).execute().also {
            expectThat(it).isOk()
        }.body()
    }

    @Step("Get Latest Build")
    fun getLatestBuild(buildConfigId: String, apiInstance: BuildApi): BuildDto? {
        await atMost aHalfOfMinute withPollInterval everyTwoSeconds until {
            getBuilds(buildConfigId, apiInstance)?.count != 0
        }
        return getBuilds(buildConfigId, apiInstance)?.build?.maxByOrNull { it.number.toString() }
    }

    @Step("Get Build Statistics")
    fun getBuildStatistics(buildConfigId: String, apiInstance: BuildApi): PropertiesDto? {
        val locator = BuildQueueLocators(BuildType(locator = Id(value = buildConfigId)))
        return apiInstance.getBuildStatisticsValues(locator).execute().also {
            expectThat(it).isOk()
        }.body()
    }

    @Step("Wait until build finished")
    fun awaitUntilBuildFinished(buildConfigId: String, apiInstance: BuildApi) {
        await atMost aHalfOfMinute withPollInterval everyTwoSeconds untilCallTo {
            getLatestBuild(buildConfigId, apiInstance)
        } has {
            state == "finished"
        }
    }

}