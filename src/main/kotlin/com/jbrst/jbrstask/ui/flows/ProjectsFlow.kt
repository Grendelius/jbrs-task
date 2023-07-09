package com.jbrst.jbrstask.ui.flows

import com.codeborne.selenide.Condition.visible
import com.codeborne.selenide.Selectors.byId
import com.codeborne.selenide.Selenide.element
import com.jbrst.jbrstask.ui.core.Page
import mu.KLogging
import org.springframework.stereotype.Component


@Component
class ProjectsFlow {
}

class ProjectsPage : Page() {

    companion object : KLogging() {
        private val searchProjectsInput = element(byId("search-projects"))
    }

    override fun validate(): ProjectsPage {
        logger.debug { "Checking the Projects Page is opened" }
        searchProjectsInput.shouldBe(visible)
        return this
    }

}