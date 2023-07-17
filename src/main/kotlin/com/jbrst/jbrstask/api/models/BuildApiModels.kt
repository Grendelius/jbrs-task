package com.jbrst.jbrstask.api.models

data class BuildDto(
    val buildTypeId: String? = null,
    val id: Long? = null,
    val taskId: Long? = null,
    val buildType: BuildTypeDto?,
    val number: String? = null,
    val status: String? = null,
    val state: String? = null
)

data class BuildsDto(
    val count: Int,
    val href: String,
    val build: List<BuildDto>
)