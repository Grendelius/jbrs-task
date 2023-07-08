package com.jbrst.jbrstask.core.config

import com.jbrst.jbrstask.core.UserData
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
@Suppress("SpringJavaInjectionPointsAutowiringInspection")
open class ProjectConfiguration(private val props: ProjectProperties) {

    @Bean
    open fun userData() = UserData(props.users)

}