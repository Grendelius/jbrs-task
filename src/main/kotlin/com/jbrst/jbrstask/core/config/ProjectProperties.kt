package com.jbrst.jbrstask.core.config

import com.jbrst.jbrstask.core.VcsRepo
import com.jbrst.jbrstask.core.models.User
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "tc")
class ProjectProperties(
    val users: List<User>,
    val vcs: List<VcsRepo>
)
