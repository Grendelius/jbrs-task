package com.jbrst.jbrstask.ui.core

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

    override fun validate(): Validatable {
        return this
    }

}