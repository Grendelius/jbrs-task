package com.jbrst.jbrstask.core.config

import com.jbrst.jbrstask.core.models.User
import com.jbrst.jbrstask.core.VcsRepo
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@ConfigurationProperties(prefix = "tc")
class ProjectProperties(
    val users: List<User>,
    val vcs: List<VcsRepo>
)
