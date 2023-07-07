package com.jbrst.jbrstask.ui.flows

import com.codeborne.selenide.Condition.visible
import com.codeborne.selenide.Selectors.byText
import com.codeborne.selenide.Selenide
import com.jbrst.jbrstask.ui.core.Page
import mu.KLogging

class ProjectsFlow {
}

class ProjectsPage : Page() {

    companion object : KLogging() {
        private val favoriteHeader = Selenide.element(byText("Favorite Projects"))
    }

    override fun validate(): ProjectsPage {
        logger.debug { "Checking the Projects Page is opened" }
        favoriteHeader.shouldBe(visible)
        return this
    }

}