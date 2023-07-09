package com.jbrst.jbrstask.core.util

import com.jbrst.jbrstask.core.models.User
import io.github.serpro69.kfaker.faker

/**
 * Factory class for creating test data structures
 */
class FakeData {

    companion object {

        private val faker = faker { }

        fun randomUsername(): String = faker.name.firstName()

        fun randomPassword(): String = faker.string.regexify("[A-Za-z0-9]")

        fun randomUser(): User = User(randomUsername(), randomPassword())

    }

}