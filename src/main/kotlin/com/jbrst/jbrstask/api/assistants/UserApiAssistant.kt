package com.jbrst.jbrstask.api.assistants

import com.jbrst.jbrstask.api.client.UsersApi
import com.jbrst.jbrstask.api.models.UserDto
import com.jbrst.jbrstask.core.isOk
import io.qameta.allure.Step
import org.springframework.stereotype.Component
import strikt.api.expectThat

@Component
class UserApiAssistant(private val usersApi: UsersApi) {

    @Step("Create a new user account")
    fun addNewUserAccount(newUser: UserDto): UserDto? {
        return usersApi.addUser(newUser).execute().also {
            expectThat(it).isOk()
        }.body()
    }

}