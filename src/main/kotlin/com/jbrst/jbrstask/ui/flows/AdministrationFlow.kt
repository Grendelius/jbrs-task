package com.jbrst.jbrstask.ui.flows

import com.codeborne.selenide.Condition.visible
import com.codeborne.selenide.Selectors.byText
import com.codeborne.selenide.Selenide.element
import com.jbrst.jbrstask.ui.core.Page
import mu.KLogging
import org.springframework.stereotype.Component

@Component
class AdministrationFlow {
}

class AdministrationPage : Page() {

    companion object : KLogging() {
        private val pageHeader = element(byText("Administration"))
    }

    override fun validate(): AdministrationPage {
        pageHeader.shouldBe(visible)
        return this
    }

}