package com.jbrst.jbrstask.api

import com.jbrst.jbrstask.BaseTest
import com.jbrst.jbrstask.api.assistants.*
import org.springframework.beans.factory.annotation.Autowired

open class BaseApiTest : BaseTest() {

    @Autowired
    protected lateinit var projectAssistant: ProjectApiAssistant

    @Autowired
    protected lateinit var buildTypeAssistant: BuildTypeApiAssistant

    @Autowired
    protected lateinit var buildQueueAssistant: BuildQueueApiAssistant

    @Autowired
    protected lateinit var vcsRootAssistant: VcsRootApiAssistant

    @Autowired
    protected lateinit var agentAssistant: AgentApiAssistant

    @Autowired
    protected lateinit var userAssistant: UserApiAssistant

}