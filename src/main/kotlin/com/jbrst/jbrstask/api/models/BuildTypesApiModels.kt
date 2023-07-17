package com.jbrst.jbrstask.api.models


data class BuildTypesDto(
    val count: Int,
    val nextHref: String,
    val prevHref: String,
    val buildType: List<BuildTypeDto>
)

data class BuildTypeDto(
    val id: String,
    val name: String,
    val projectName: String,
    val projectId: String,
    val href: String?,
    val webUrl: String?
)

data class StepDto(
    val id: String, val name: String, val type: String, val disabled: Boolean = false, val inherited: Boolean = false,
    val href: String = "", val properties: PropertiesDto
)