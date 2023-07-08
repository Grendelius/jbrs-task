package com.jbrst.jbrstask

import com.jbrst.jbrstask.core.config.ProjectProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties

@EnableConfigurationProperties(ProjectProperties::class)
@SpringBootApplication(scanBasePackages = ["com.jbrst.jbrstask"])
open class SpringTestApplication