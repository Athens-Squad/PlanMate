package logic.use_cases.project

import com.google.common.truth.Truth.assertThat
import helper.project_helper.createProject
import helper.project_helper.fakes.FakeProjectData
import helper.project_helper.fakes.FakeProjectData.adminUser
import helper.project_helper.fakes.FakeProjectData.mateUserForAdminUser
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import logic.entities.Project
import logic.repositories.AuditRepository
import logic.repositories.ProjectsRepository
import logic.repositories.UserRepository
import data.projects.exceptions.ProjectsLogicExceptions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class DeleteProjectUseCaseTest {
    private val projectRepository: ProjectsRepository = mockk(relaxed = true)
    private val userRepository: UserRepository = mockk(relaxed = true)

    private val auditRepository: AuditRepository = mockk(relaxed = true)
    private lateinit var fakeProject: Project

    private lateinit var deleteProjectUseCase: DeleteProjectUseCase

    @BeforeEach
    fun setUp() {
        fakeProject = createProject().copy(
            id = "1",
            states = FakeProjectData.states,
            tasks = FakeProjectData.tasks
        )

        deleteProjectUseCase = DeleteProjectUseCase(projectRepository, userRepository,auditRepository)
    }

    @Test
    fun `should delete project successfully, when project is valid`() {
        val validProject = fakeProject.copy(
            createdBy = adminUser.name
        )
        every { userRepository.getUserByUsername(adminUser.name) } returns Result.success(adminUser)
        every { projectRepository.getProjects() } returns Result.success(listOf(validProject))
        every { projectRepository.deleteProject(validProject.id) } returns Result.success(Unit)
        every { auditRepository.createAuditLog(any()) } returns Result.success(Unit)

        val result = deleteProjectUseCase.execute(adminUser.name, validProject.id)

        assertThat(result.isSuccess).isTrue()
        verify(exactly = 1) { userRepository.getUserByUsername(adminUser.name) }
        verify(exactly = 1) { projectRepository.getProjects() }
        verify(exactly = 1) { projectRepository.deleteProject(validProject.id) }
        verify(exactly = 1) { auditRepository.createAuditLog(any()) }
    }

    @Test
    fun `should delete project failed, when user is not the owner of project`() {
        val invalidProject = fakeProject.copy(
            createdBy = "diffrent user"
        )
        every { userRepository.getUserByUsername(adminUser.name) } returns Result.success(adminUser)
        every { projectRepository.getProjects() } returns Result.success(listOf(invalidProject))
        every { projectRepository.deleteProject(invalidProject.id) } returns Result.success(Unit)

        assertThrows<NotAuthorizedUserException> {
            deleteProjectUseCase.execute(adminUser.name, invalidProject.id).getOrThrow()
        }
        verify(exactly = 1) { userRepository.getUserByUsername(adminUser.name) }
        verify(exactly = 1) { projectRepository.getProjects() }
        verify(exactly = 0) { projectRepository.deleteProject(invalidProject.id) }
    }

    @Test
    fun `should delete project failed and throw exception, when username is invalid`() {
        val invalidAdminUserName = ""
        every { userRepository.getUserByUsername(invalidAdminUserName) } returns Result.failure(
            Exception()
        )

        val result = deleteProjectUseCase.execute(invalidAdminUserName, fakeProject.id)

        assertThat(result.exceptionOrNull()).isInstanceOf(InvalidUsernameForProjectException::class.java)
        verify(exactly = 0) { userRepository.getUserByUsername(invalidAdminUserName) }
        verify(exactly = 0) { projectRepository.getProjects() }
        verify(exactly = 0) { projectRepository.deleteProject(fakeProject.id) }
    }

    @Test
    fun `should delete project failed and throw exception, when user not found to delete project`() {
        val notExistingUsername = "usernameDoNotExist"
        every { userRepository.getUserByUsername(notExistingUsername) } returns Result.failure(
            Exception()
        )
        every { projectRepository.getProjects() } returns Result.success(listOf(fakeProject))

        val result = deleteProjectUseCase.execute(notExistingUsername, fakeProject.id)

        assertThat(result.exceptionOrNull()).isInstanceOf(NotAuthorizedUserException::class.java)
        verify(exactly = 0) { projectRepository.deleteProject(fakeProject.id) }
        verify(exactly = 0) { projectRepository.getProjects() }
        verify(exactly = 1) { userRepository.getUserByUsername(notExistingUsername) }
    }

    @Test
    fun `should delete project failed and throw exception, when user is not admin`() {
        every { userRepository.getUserByUsername(mateUserForAdminUser.name) } returns Result.success(mateUserForAdminUser)
        every { projectRepository.getProjects() } returns Result.success(listOf(fakeProject))

        val result = deleteProjectUseCase.execute(mateUserForAdminUser.name, fakeProject.id)

        assertThat(result.exceptionOrNull()).isInstanceOf(NotAuthorizedUserException::class.java)
        verify(exactly = 0) { projectRepository.deleteProject(fakeProject.id) }
        verify(exactly = 1) { userRepository.getUserByUsername(mateUserForAdminUser.name) }
    }

    @Test
    fun `should delete project failed and throw exception, when project not found for deleted project`() {
        val notExistingProjectId = "projectIdDoNotExist"
        every { userRepository.getUserByUsername(adminUser.name) } returns Result.success(adminUser)
        every { projectRepository.getProjects() } returns Result.success(listOf(fakeProject))

        val result = deleteProjectUseCase.execute(adminUser.name, notExistingProjectId)

        assertThat(result.exceptionOrNull()).isInstanceOf(NoProjectFoundException::class.java)
        verify(exactly = 0) { projectRepository.deleteProject(notExistingProjectId) }
        verify(exactly = 1) { projectRepository.getProjects() }
        verify(exactly = 1) { userRepository.getUserByUsername(adminUser.name) }
    }

    @Test
    fun `should delete project failed and throw exception, when project id is invalid`() {
        val invalidProjectId = ""
        every { userRepository.getUserByUsername(adminUser.name) } returns Result.success(adminUser)
        every { projectRepository.getProjects() } returns Result.success(listOf(fakeProject))

        val result = deleteProjectUseCase.execute(adminUser.name, invalidProjectId)

        assertThat(result.exceptionOrNull()).isInstanceOf(NoProjectFoundException::class.java)
        verify(exactly = 0) { projectRepository.deleteProject(invalidProjectId) }
        verify(exactly = 0) { projectRepository.getProjects() }
        verify(exactly = 1) { userRepository.getUserByUsername(adminUser.name) }
    }
}