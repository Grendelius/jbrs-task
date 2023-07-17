package com.jbrst.jbrstask.ui.flows

import com.codeborne.selenide.ClickOptions
import com.codeborne.selenide.Condition.*
import com.codeborne.selenide.Selectors.*
import com.codeborne.selenide.Selenide.element
import com.jbrst.jbrstask.core.models.User
import com.jbrst.jbrstask.ui.core.Page
import com.jbrst.jbrstask.ui.core.Validatable
import com.jbrst.jbrstask.ui.flows.AdministrationPage.Companion.ProjectRelatedSettings.projectsLink
import com.jbrst.jbrstask.ui.flows.AdministrationPage.Companion.ServerAdministration.usersLink
import com.jbrst.jbrstask.ui.flows.CreateObjectMenuPage.Companion.createManually
import com.jbrst.jbrstask.ui.flows.CreateObjectMenuPage.Companion.fromVcsRepoLink
import com.jbrst.jbrstask.ui.flows.ProjectsItemPage.Companion.createNewProjectBtn
import com.jbrst.jbrstask.ui.flows.UsersItemPage.Companion.createNewUserBtn
import com.jbrst.jbrstask.ui.models.FromRepoProjectBuildEntry
import com.jbrst.jbrstask.ui.models.ManualProjectBuildEntry
import io.qameta.allure.Step
import mu.KLogging
import org.springframework.stereotype.Component

@Component
class AdministrationFlow {

    @Step("Create a new project manually from Administration Page")
    fun createProjectManually(projectEntry: ManualProjectBuildEntry): ProjectEntityPage {
        return Page.on<AdministrationPage>()
            .clickOn(projectsLink)
            .on<ProjectsItemPage>()
            .clickOn(createNewProjectBtn)
            .clickOn(createManually)
            .on<CreateObjectMenuPage>()
            .inputProjectName(projectEntry.projectName)
            .inputProjectId(projectEntry.projectId)
            .create()
            .on<ProjectEntityPage>()
    }

    @Step("Create a new project based on a VCS URL from Administration Page")
    fun createProjectFromVcsUrl(projectEntry: FromRepoProjectBuildEntry): ObjectSetupPage {
        return Page.on<AdministrationPage>()
            .clickOn(projectsLink)
            .on<ProjectsItemPage>()
            .clickOn(createNewProjectBtn)
            .clickOn(fromVcsRepoLink)
            .on<CreateObjectMenuPage>()
            .fillVcsFields(projectEntry.vcsRepo)
            .proceed()
            .on<ObjectSetupPage>()
    }

    @Step("Create a new user account")
    fun createUserAccount(user: User, isAdmin: Boolean): UsersItemPage {
        return Page.on<AdministrationPage>()
            .clickOn(usersLink)
            .on<UsersItemPage>()
            .clickOn(createNewUserBtn)
            .on<CreateNewUserAccountForm>()
            .fillMandatoryFields(user, isAdmin)
            .submitForm()
            .on<UsersItemPage>()
    }

}

class AdministrationPage : Page() {

    companion object : KLogging() {
        private val pageHeader = element(byText("Administration"))

        object ProjectRelatedSettings {
            internal val projectsLink = element(byText("Projects"))
            internal val allBuildsLink = element(byText("All Builds"))
            internal val auditLink = element(byText("Audit"))
        }

        object ServerAdministration {
            internal val usersLink = element(byText("Users"))
            internal val backupLink = element(byText("Backup"))
        }

    }

    override fun validate(): AdministrationPage {
        logger.info { "Checking that the Administration Page is open" }
        pageHeader.shouldBe(visible)
        return this
    }

}

class ProjectsItemPage : Page() {

    companion object : KLogging() {
        internal val createNewProjectBtn = element(byXpath("//p[@class = 'createProject']//span"))
        private val allProjectsTable = element(byId("all-projects"))
    }

    override fun validate(): Validatable {
        logger.debug { "Checking Projects page is open. " }
        allProjectsTable.shouldBe(visible)
        return this
    }

}

class CreateNewUserAccountForm : Page() {

    companion object : KLogging() {
        private val usernameInput = element(byId("input_teamcityUsername"))
        private val newPasswordInput = element(byId("password1"))
        private val confirmPasswordInput = element(byId("retypedPassword"))
        private val newEmailInput = element(byId("input_teamcityEmail"))
        private val isAdminCheckbox = element(byId("administrator"))
        private val submitBtn = element(byName("submitCreateUser"))
    }

    fun fillMandatoryFields(user: User, isAdmin: Boolean): CreateNewUserAccountForm {
        inputUserName(user.username)
            .inputAndConfirmPassword(user.password)
            .checkIsAdmin(isAdmin)
        return this
    }

    fun inputUserName(username: String): CreateNewUserAccountForm {
        usernameInput.`val`(username)
        return this
    }

    fun inputAndConfirmPassword(password: String): CreateNewUserAccountForm {
        newPasswordInput.`val`(password)
        confirmPasswordInput.`val`(password)
        return this
    }

    fun inputEmail(email: String): CreateNewUserAccountForm {
        newEmailInput.`val`(email)
        return this
    }

    fun checkIsAdmin(isAdmin: Boolean): CreateNewUserAccountForm {
        if (isAdmin) isAdminCheckbox.click(ClickOptions.usingDefaultMethod()).shouldBe(checked)
        return this
    }

    fun submitForm(): Page {
        submitBtn.submit()
        return this
    }

    override fun validate(): Validatable {
        logger.debug { "Checking the Create New Account Page is open." }
        usernameInput.shouldBe(visible)
        return this
    }

}

class UsersItemPage : Page() {

    companion object : KLogging() {
        internal val createNewUserBtn = element(byText("Create user account"))
        private val successMessageBlock = element(byClassName("successMessage"))
        private val userTable = element(byId("userTableInner"))
    }

    fun successMessageIsVisible(message: String) {
        successMessageBlock.shouldHave(ownText(message.trim()))
    }

    override fun validate(): Validatable {
        logger.debug { "Checking the Users Page is open." }
        userTable.shouldBe(interactable)
        return this
    }

}