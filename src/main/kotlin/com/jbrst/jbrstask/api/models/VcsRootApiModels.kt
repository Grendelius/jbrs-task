package com.jbrst.jbrstask.api.models

import com.google.gson.annotations.SerializedName


data class VcsRootsDto(
    val count: Int,
    val href: String,
    val nextHref: String,
    val prevHref: String,
    @SerializedName("vcs-root")
    val vcsRoot: List<VcsRootDto>
)

data class VcsRootDto(
    val id: String,
    val href: String,
    val vcsName: String = "jetbrains.git",
    val properties: PropertiesDto,
    val project: ProjectDto? = null
)

data class VcsRootEntry(
    val id: String,
    val inherited: Boolean,
    @SerializedName("vcs-root") val vcsRoot: VcsRootDto,
    @SerializedName("checkout-rules") val checkoutRules: String?
)