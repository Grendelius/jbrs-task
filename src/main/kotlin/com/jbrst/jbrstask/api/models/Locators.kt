package com.jbrst.jbrstask.api.models


data class LocatorDto(val locator: String)

class Locators(vararg val locators: Locator) {

    override fun toString(): String {
        return if (locators.size > 1) locators.joinToString(",") else locators[0].toString()
    }

}

class BuildQueueLocators(vararg val locators: BuildQueueLocator) {

    override fun toString(): String {
        return if (locators.size > 1) locators.joinToString(",") else locators[0].toString()
    }

}

abstract class Locator {

    abstract val key: String
    abstract val value: String

    override fun toString(): String {
        return "$key:$value"
    }

}

abstract class BuildQueueLocator {

    abstract val buildQueueKey: String
    abstract val locator: Locator

    override fun toString(): String {
        return "$buildQueueKey(${locator.key}:${locator.value})"
    }

}

class BuildType(override val buildQueueKey: String = "buildType", override val locator: Locator) : BuildQueueLocator()

class Id(override val key: String = "id", override val value: String) : Locator()

class Connected(override val key: String = "connected", override val value: String) : Locator()

class Authorized(override val key: String = "authorized", override val value: String) : Locator()

class Enabled(override val key: String = "enabled", override val value: String) : Locator()