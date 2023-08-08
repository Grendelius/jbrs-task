package com.jbrst.jbrstask.api.client

import com.jbrst.jbrstask.api.constants.ACCEPT_DEFAULT
import com.jbrst.jbrstask.api.models.BuildQueueLocators
import com.jbrst.jbrstask.api.models.BuildsDto
import com.jbrst.jbrstask.api.models.PropertiesDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface BuildApi {

    @Headers(value = [ACCEPT_DEFAULT])
    @GET("/app/rest/builds")
    fun getRecentBuilds(@Query("locator") locators: BuildQueueLocators): Call<BuildsDto>

    @Headers(value = [ACCEPT_DEFAULT])
    @GET("/app/rest/builds/{btLocator}/statistics")
    fun getBuildStatisticsValues(@Path("btLocator") locators: BuildQueueLocators): Call<PropertiesDto>

}