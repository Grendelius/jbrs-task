package com.jbrst.jbrstask.core.util

import com.jbrst.jbrstask.core.models.User
import io.github.serpro69.kfaker.faker

class TestDataCreator {

    companion object {
        private val faker = faker { }
    }

    object UserData {

        fun randomUsername(): String = faker.name.firstName()

        fun randomPassword(): String = faker.string.regexify("[A-Za-z0-9")

        fun randomUser(): User = User(randomUsername(), randomPassword())

    }

    object TcProjectData {

    }

}