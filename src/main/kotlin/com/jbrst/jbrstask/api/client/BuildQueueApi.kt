package com.jbrst.jbrstask.api.client

import com.jbrst.jbrstask.api.constants.ACCEPT_DEFAULT
import com.jbrst.jbrstask.api.constants.CONTENT_TYPE_DEFAULT
import com.jbrst.jbrstask.api.models.BuildDto
import com.jbrst.jbrstask.api.models.BuildQueueLocator
import com.jbrst.jbrstask.api.models.BuildsDto
import retrofit2.Call
import retrofit2.http.*


interface BuildQueueApi {

    @Headers(value = [ACCEPT_DEFAULT, CONTENT_TYPE_DEFAULT])
    @POST("/app/rest/buildQueue")
    fun addBuildToQueue(@Body buildTypeToCreate: BuildDto): Call<BuildDto>

    @Headers(value = [ACCEPT_DEFAULT])
    @GET("/app/rest/buildQueue")
    fun getQueuedBuilds(@Query("locator") locator: BuildQueueLocator): Call<BuildsDto>

}
