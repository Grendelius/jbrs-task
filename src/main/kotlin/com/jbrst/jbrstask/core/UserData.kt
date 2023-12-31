package com.jbrst.jbrstask.core

import com.jbrst.jbrstask.core.exceptions.illegalArgument
import com.jbrst.jbrstask.core.models.User

data class UserData(
    private val users: List<User>
) {
    fun getByUsername(username: String): User = users.find { it.username == username } ?: illegalArgument(
        "User [$username] is not found in the properties"
    )

    fun admin(): User = users.first { it.username == "admin" }

    fun superAdmin(): User = users.first { it.username.isBlank() }

}