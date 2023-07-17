package com.jbrst.jbrstask.ui.flows

import com.codeborne.selenide.ClickOptions
import com.codeborne.selenide.Condition.*
import com.codeborne.selenide.Selectors.*
import com.codeborne.selenide.Selenide
import com.codeborne.selenide.Selenide.element
import com.jbrst.jbrstask.core.VcsRepo
import com.jbrst.jbrstask.ui.core.Page
import com.jbrst.jbrstask.ui.core.Validatable
import com.jbrst.jbrstask.ui.flows.CreateObjectMenuPage.Companion.createManually
import com.jbrst.jbrstask.ui.flows.CreateObjectMenuPage.Companion.fromVcsRepoLink
import com.jbrst.jbrstask.ui.flows.ProjectsPage.Companion.createProjectsPageBtn
import com.jbrst.jbrstask.ui.models.FromRepoProjectBuildEntry
import com.jbrst.jbrstask.ui.models.ManualProjectBuildEntry
import io.qameta.allure.Step
import mu.KLogging
import org.springframework.stereotype.Component


@Component
class ProjectsFlow {

    @Step("Delete the Project")
    fun deleteProject(projectId: String): EditProjectPage {
        Selenide.open("/admin/editProject.html?projectId=$projectId")
        return Page.on<EditProjectPage>()
            .clickOnAction("Delete project")
            .on<EditProjectPage>()
    }

    @Step("Open the Project")
    fun openProject(projectName: String) {
        Selenide.open("/project/$projectName")
        Page.on<ProjectEntityPage>()
    }

    @Step("Create project manually")
    fun createProjectManually(projectEntry: ManualProjectBuildEntry): ProjectEntityPage {
        return Page.on<ProjectsPage>()
            .clickOn(createProjectsPageBtn)
            .clickOn(createManually)
            .on<CreateObjectMenuPage>()
            .inputProjectName(projectEntry.projectName)
            .inputProjectId(projectEntry.projectId)
            .create()
            .on<ProjectEntityPage>()
    }

    @Step("Create project based on a VCS URL")
    fun createProjectFromVcsUrl(projectEntry: FromRepoProjectBuildEntry): ObjectSetupPage {
        return Page.on<ProjectsPage>()
            .clickOn(createProjectsPageBtn)
            .clickOn(fromVcsRepoLink)
            .on<CreateObjectMenuPage>()
            .fillVcsFields(projectEntry.vcsRepo)
            .proceed()
            .on<ObjectSetupPage>()
    }
}

class EditProjectPage : Page() {

    companion object : KLogging() {
        private val editProjectForm = element(byId("editProjectForm"))
        private val projectRemovedMsg = element(byId("message_projectRemoved"))
        internal val actionsDropdownBtn =
            element(byXpath(".//i[@class = 'icon-caret-down' and ancestor::button[normalize-space() = 'Actions']]"))
    }

    fun clickOnAction(actionName: String): EditProjectPage {
        actionsDropdownBtn.click(ClickOptions.usingDefaultMethod())
        element(byPartialLinkText(actionName)).hover().click()
        return this
    }

    fun projectDeletedMessageIsDisplayed(msg: String): EditProjectPage {
        projectRemovedMsg.shouldHave(text(msg))
        return this
    }

    override fun validate(): Validatable {
        logger.debug { "Checking the Edit Project page is open." }
        editProjectForm.shouldBe(visible)
        return this
    }

}

class ProjectsPage : Page() {

    companion object : KLogging() {
        private val searchProjectsInput = element(byId("search-projects"))
        internal val createProjectsPageBtn =
            element(byXpath("//a[@data-test = 'create-project'] | //a[@data-hint-container-id = 'project-create-entity']"))
    }

    override fun validate(): ProjectsPage {
        logger.debug { "Checking the Projects Page is open" }
        searchProjectsInput.shouldBe(visible)
        return this
    }

}

class CreateObjectMenuPage : Page() {

    companion object : KLogging() {
        val fromVcsRepoLink = element(byXpath("//a[@href = '#createFromUrl']"))
        val createManually = element(byXpath("//a[@href = '#createManually']"))
        private val headerLink = element(byXpath("//a[text() = 'Create Project']"))
        private val vcsRepoInput = element(byId("url"))
        private val vcsUsernameInput = element(byId("username"))
        private val vcsPasswordInput = element(byId("password"))
        private val proceedButton = element(byValue("Proceed"))
        private val createProjectBtn = element(byId("createProject"))
        private val projectNameInput = element(byId("name"))
        private val projectIdInput = element(byId("externalId"))
    }

    fun inputProjectName(projectName: String): CreateObjectMenuPage {
        projectNameInput.`val`(projectName)
        return this
    }

    fun inputProjectId(projectId: String): CreateObjectMenuPage {
        projectIdInput.`val`(projectId)
        return this
    }

    fun inputVcsRepoUrl(vcsRepoUrl: String): CreateObjectMenuPage {
        vcsRepoInput.`val`(vcsRepoUrl)
        return this
    }

    fun inputVcsUsername(username: String): CreateObjectMenuPage {
        vcsUsernameInput.`val`(username)
        return this
    }

    fun inputVcsPasswordToken(pswToken: String): CreateObjectMenuPage {
        vcsPasswordInput.`val`(pswToken)
        return this
    }

    fun fillVcsFields(vcsRepo: VcsRepo): CreateObjectMenuPage {
        inputVcsRepoUrl("${vcsRepo.rootUrl}${vcsRepo.project}.git")
            .inputVcsUsername(vcsRepo.username)
            .inputVcsPasswordToken(vcsRepo.password)
        return this
    }

    fun create(): Page {
        createProjectBtn.submit()
        return this
    }

    fun proceed(): Page {
        proceedButton.submit()
        return this
    }

    override fun validate(): Validatable {
        logger.debug { "Checking the Create Project page is open." }
        headerLink.shouldBe(visible)
        return this
    }

}

class ObjectSetupPage : Page() {

    data class ProjectObjectProps(
        val projectName: String,
        val defaultBuildConfigName: String? = null,
        val defaultBranchRef: String? = null,
        val defaultBranchSpec: String? = null
    )

    companion object : KLogging() {
        private val headerLink = element(byXpath("//a[normalize-space()='Create Project From URL']"))
        private val proceedButton = element(byValue("Proceed"))
        private val branchInput = element(byId("branch"))
        private val branchSpecTextArea = element(byId("teamcity:branchSpec"))
        private val projectNameInput = element(byId("projectName"))
        private val buildTypeNameInput = element(byId("buildTypeName"))
        private val successfulMsgBlock = element(byClassName("connectionSuccessful"))
    }

    fun proceed(): Page {
        proceedButton.submit()
        return this
    }

    fun inputDefaultBranch(branchNameRef: String): ObjectSetupPage {
        branchInput.`val`(branchNameRef)
        return this
    }

    fun inputProjectName(projectName: String): ObjectSetupPage {
        projectNameInput.`val`(projectName)
        return this
    }

    fun inputBuildConfigName(buildTypeName: String): ObjectSetupPage {
        buildTypeNameInput.`val`(buildTypeName)
        return this
    }

    fun successMessageIsDisplayed(msg: String): ObjectSetupPage {
        successfulMsgBlock.shouldBe(matchText(msg))
        return this
    }

    override fun validate(): Validatable {
        logger.debug { "Checking the Create Project From Url page is open." }
        headerLink.shouldBe(visible)
        return this
    }

}

class ProjectEntityPage : Page() {

    companion object : KLogging() {
        private val successMsgBlock = element(byClassName("successMessage"))
        private val projectPageHeader = element(byXpath("//span[contains(@class,'ProjectPageHeader')]"))
        private val tab = element(byAttribute("data-test", "tab"))
    }

    fun successMessageIsDisplayed(msg: String) {
        successMsgBlock.shouldBe(matchText(msg))
    }

    override fun validate(): Validatable {
        logger.debug { "Checking the Project page is open." }
        projectPageHeader.shouldBe(visible)
        tab.shouldHave(ownText("Overview"))
        return this
    }

}