package com.jbrst.jbrstask.ui.core

import com.codeborne.selenide.Selenide
import com.codeborne.selenide.SelenideElement

open class Page : Validatable {

    companion object {
        inline fun <reified T : Page> on(): T {
            return Page().on()
        }
    }

    inline fun <reified T : Page> on(): T {
        val page = T::class.constructors.first().call()
        page.validate()
        return page
    }

    fun clickOn(element: SelenideElement): Page {
        element.hover().click()
        return this
    }

    fun back(): Page {
        Selenide.back()
        return this
    }

    override fun validate(): Validatable {
        return this
    }

}