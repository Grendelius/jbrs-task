package com.jbrst.jbrstask.api.client

import com.jbrst.jbrstask.api.constants.ACCEPT_DEFAULT
import com.jbrst.jbrstask.api.constants.CONTENT_TYPE_DEFAULT
import com.jbrst.jbrstask.api.models.Locator
import com.jbrst.jbrstask.api.models.UserDto
import com.jbrst.jbrstask.api.models.UsersDto
import retrofit2.Call
import retrofit2.http.*

interface UsersApi {

    @Headers(value = [ACCEPT_DEFAULT, CONTENT_TYPE_DEFAULT])
    @POST("/app/rest/users")
    fun addUser(@Body userToAdd: UserDto): Call<UserDto>

    @Headers(value = [ACCEPT_DEFAULT])
    @GET("/app/rest/users")
    fun listUsers(): Call<UsersDto>

    @Headers(value = [ACCEPT_DEFAULT])
    @DELETE("/app/rest/users/{id}")
    fun removeUser(@Path("id") id: Locator): Call<String>

}