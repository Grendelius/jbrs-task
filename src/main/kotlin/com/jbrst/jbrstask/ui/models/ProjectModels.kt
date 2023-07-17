package com.jbrst.jbrstask.ui.models

import com.jbrst.jbrstask.core.VcsRepo

sealed class AbstractProjectBuildEntry(val parentProject: String? = "<Root project>")

data class FromRepoProjectBuildEntry(
    val vcsRepo: VcsRepo
) : AbstractProjectBuildEntry()

data class ManualProjectBuildEntry(
    val projectId: String,
    val projectName: String,
    val description: String
) :
    AbstractProjectBuildEntry()