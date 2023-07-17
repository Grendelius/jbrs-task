package com.jbrst.jbrstask.core.testdata

import com.jbrst.jbrstask.api.models.*
import com.jbrst.jbrstask.core.*
import com.jbrst.jbrstask.core.VcsType.GIT
import com.jbrst.jbrstask.core.models.User
import com.jbrst.jbrstask.ui.models.FromRepoProjectBuildEntry
import io.github.serpro69.kfaker.faker
import org.apache.commons.lang3.RandomStringUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*

@Component
class TestDataProvider(val userData: UserData, val vcsData: VcsData) {

    @Value("\${tc.server.api.host}")
    private lateinit var apiHost: String

    internal val faker = faker { }

    fun fakeUsername(): String = faker.name.firstName()

    fun fakePassword(): String = faker.string.regexify("[AbcdeZ^(145]")

    fun fakeEmail(): String = faker.internet.safeEmail()

    fun fakeUser(): User = User(fakeUsername(), fakePassword(), fakeEmail())

    fun fakeProjectName(): String = RandomStringUtils.randomAlphabetic(5)

    fun fakeProjectId(): String = fakeProjectName().replaceFirstChar { it.titlecaseChar() }

    fun fakeBuildName(): String = faker.app.name()

    private fun getVcsDataByType(type: VcsType) {
        vcsData.repos.first { vcs -> vcs.type == type }
    }

    fun gitRepoProjectEntry(): FromRepoProjectBuildEntry {
        return FromRepoProjectBuildEntry(vcsData.repos.first { vcs -> vcs.type == GIT })
    }

    fun buildTypeEntry(projectName: String): BuildTypeDto {
        val buildConfigName = "UnitTests"
        val buildId = "${projectName}_${buildConfigName}"
        val buildHref = "/app/rest/buildTypes/id:${buildId}"
        val buildWebUrl = "https1`://$apiHost/viewType.html?buildTypeId=${buildId}"

        return BuildTypeDto(
            id = buildId,
            name = buildConfigName,
            projectName = projectName,
            projectId = projectName.replaceFirstChar { it.titlecaseChar() },
            href = buildHref,
            webUrl = buildWebUrl
        )
    }

    fun buildTestMavenStep(): StepDto {
        return StepDto(
            "RUNNER_${faker.string.numerify("1")}",
            "MavenStep",
            "Maven2",
            properties = PropertiesDto(mvnStepProperties()),
        )
    }

    fun buildVcsRoot(projectId: String): VcsRootDto {
        val vcsRepoEntry = gitRepoProjectEntry().vcsRepo
        val vscRootId = RandomStringUtils.randomAlphabetic(5)
        return VcsRootDto(
            id = vscRootId,
            href = vcsRepoEntry.fullUrl(),
            properties = PropertiesDto(gitVcsProperties()),
            project = ProjectDto(id = projectId)
        )
    }

    fun buildVcsRootEntry(vscRoot: VcsRootDto): VcsRootEntry {
        return VcsRootEntry(
            id = vscRoot.id,
            true,
            vcsRoot = vscRoot,
            checkoutRules = ""
        )
    }

    fun newAdminUser(): UserDto {
        val randomUser = fakeUser()
        return UserDto(
            username = randomUser.username,
            password = randomUser.password,
            email = randomUser.email,
            roles = RolesDto(role = listOf(RoleDto(UserRole.SYSTEM_ADMIN.name, "g")))
        )
    }

    fun mvnStepProperties(): List<PropertyDto> {
        return listOf(
            ("goals" to "clean test").toProperty(),
            ("localRepoScope" to "agent").toProperty(),
            ("maven.path" to "%teamcity.tool.maven.DEFAULT%").toProperty(),
            ("pomLocation" to "pom.xml").toProperty(),
            ("teamcity.step.mode" to "default").toProperty(),
            ("userSettingsSelection" to "userSettingsSelection:default").toProperty()
        )
    }

    fun gitVcsProperties(): List<PropertyDto> {
        val gitVcsRepo = gitRepoProjectEntry().vcsRepo
        return listOf(
            ("agentCleanFilesPolicy" to "ALL_UNTRACKED").toProperty(),
            ("agentCleanPolicy" to "ON_BRANCH_CHANGE").toProperty(),
            ("authMethod" to "PASSWORD").toProperty(),
            ("branch" to "refs/heads/main").toProperty(),
            ("ignoreKnownHosts" to "true").toProperty(),
            ("secure:password" to gitVcsRepo.password).toProperty(),
            ("submoduleCheckout" to "CHECKOUT").toProperty(),
            ("teamcity:branchSpec" to "refs/heads/*").toProperty(),
            ("url" to gitVcsRepo.fullUrl()).toProperty(),
            ("useAlternates" to "AUTO").toProperty(),
            ("username" to gitVcsRepo.username).toProperty(),
            ("usernameStyle" to "USERID").toProperty()
        )
    }

    private fun Pair<String, Any?>.toProperty(): PropertyDto {
        return PropertyDto(first, second)
    }

}