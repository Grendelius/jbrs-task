package com.jbrst.jbrstask.core

enum class VcsType {
    GIT,
    GIT_LAB
}

data class VcsRepo(
    val type: VcsType,
    val rootUrl: String,
    val project: String,
    val username: String,
    val password: String
)