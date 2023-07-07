package com.jbrst.jbrstask.core.config

import org.aeonbits.owner.Config
import org.aeonbits.owner.Config.*
import org.aeonbits.owner.Config.LoadType.MERGE
import org.aeonbits.owner.ConfigFactory

object TeamCityConfiguration {

    val tcConfigData: TcConfigData by lazy {
        val props = ConfigFactory.create(ConfigProperties::class.java)

        TcConfigData(
            props.tcServer(),
            props.tcUsername(),
            props.tcPassword(),
            props.tcToken()
        )
    }

    class TcConfigData(val tcServer: String, val tcUsername: String, val tcPassword: String, val tcToken: String)

}


@LoadPolicy(MERGE)
@Sources("classpath:config.properties", "system:properties", "system:env")
private interface ConfigProperties : Config {

    @Key("tc.server.url")
    fun tcServer(): String

    @Key("tc.server.username")
    fun tcUsername(): String

    @Key("tc.server.password")
    fun tcPassword(): String

    @Key("tc.server.token")
    fun tcToken(): String

}