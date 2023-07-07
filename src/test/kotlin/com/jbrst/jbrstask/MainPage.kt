package com.jbrst.jbrstask

import com.codeborne.selenide.Selectors.byXpath
import com.codeborne.selenide.Selenide.element
import com.jbrst.jbrstask.ui.core.Page

// page_url = https://www.jetbrains.com/
class MainPage : Page() {

    val seeDeveloperToolsButton = element(byXpath("//*[@data-test-marker='Developer Tools']"))
    val findYourToolsButton = element(byXpath("//*[@data-test='suggestion-action']"))
    val toolsMenu = element(byXpath("//div[@data-test='main-menu-item' and @data-test-marker = 'Developer Tools']"))
    val searchButton = element("[data-test='site-header-search-action']")


}
