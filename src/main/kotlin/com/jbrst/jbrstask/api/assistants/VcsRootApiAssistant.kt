package com.jbrst.jbrstask.api.assistants

import com.jbrst.jbrstask.api.client.VcsRootApi
import com.jbrst.jbrstask.api.models.VcsRootDto
import com.jbrst.jbrstask.core.isOk
import io.qameta.allure.Step
import org.springframework.stereotype.Component
import strikt.api.expectThat

@Component
class VcsRootApiAssistant(private val vcsRootApi: VcsRootApi) {

    @Step("Add a new Vcs Root")
    fun addNewVcsRoot(vcsRootToCreate: VcsRootDto): VcsRootDto? {
        return vcsRootApi.addVcsRoot(vcsRootToCreate).execute().also {
            expectThat(it).isOk()
        }.body()
    }

}