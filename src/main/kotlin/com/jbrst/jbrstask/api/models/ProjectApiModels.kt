package com.jbrst.jbrstask.api.models


data class ProjectsDto(val count: Int, val href: String, val project: List<ProjectDto>)

data class ProjectFeaturesDto(val count: Int, val href: String, val projectFeature: List<ProjectFeatureDto>)

data class ProjectFeatureDto(
    val id: String,
    val name: String,
    val type: String,
    val disabled: Boolean,
    val inherited: Boolean,
    val href: String,
    val properties: Any
)

data class ProjectDto(
    val id: String,
    val name: String? = null,
    val description: String? = null,
    val href: String? = null,
    val webUrl: String? = null
)

data class NewProjectDescriptionDto(
    val id: String,
    val name: String,
    val parentProject: LocatorDto = LocatorDto(locator = "_Root"),
    val copyAllAssociatedSettings: Boolean = false
)

data class ProjectDetailsDto(
    val id: String,
    val name: String,
    val parentProjectId: String,
    val buildTypes: BuildTypesDto,
    val vcsRoots: VcsRootsDto,
    val projectFeatures: ProjectFeaturesDto,
    val projects: ProjectsDto
)