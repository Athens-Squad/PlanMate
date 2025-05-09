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
import logic.repositories.*
import net.thechance.logic.exceptions.ProjectsLogicExceptions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class UpdateProjectUseCaseTest {

    private val projectRepository: ProjectsRepository = mockk(relaxed = true)
    private val userRepository: UserRepository = mockk(relaxed = true)

    private val auditRepository: AuditRepository = mockk(relaxed = true)
    private lateinit var fakeProject: Project
    private lateinit var updatedProject: Project
    private lateinit var updateProjectUseCase: UpdateProjectUseCase

    @BeforeEach
    fun setUp() {
        fakeProject = createProject().copy(
            id = "1",
            progressionStates = FakeProjectData.states,
            tasks = FakeProjectData.tasks,
        )
        updatedProject = fakeProject.copy(
            name = "Updated Project",
            description = "Just a simple updated project",
        )

        updateProjectUseCase =
            UpdateProjectUseCase(projectRepository, userRepository, auditRepository)
    }

    @Test
    fun `should update project successfully, when update is valid`() {
        every { userRepository.getUserByUsername(updatedProject.createdBy) } returns Result.success(adminUser)
        every { projectRepository.getProjects() } returns Result.success(listOf(fakeProject))
        every { projectRepository.updateProject(updatedProject) } returns Result.success(Unit)
        every { auditRepository.createAuditLog(any()) } returns Result.success(Unit)

        val result = updateProjectUseCase.execute(updatedProject)

        assertThat(result.isSuccess).isTrue()
        verify(exactly = 1) { projectRepository.updateProject(updatedProject) }
        verify(exactly = 1) { projectRepository.getProjects() }
        verify(exactly = 1) { userRepository.getUserByUsername(updatedProject.createdBy) }
        verify(exactly = 1) { auditRepository.createAuditLog(any()) }
    }

    @Test
    fun `should update project failed and throw exception, when user is not admin`() {
        every { userRepository.getUserByUsername(updatedProject.createdBy) } returns Result.success(mateUserForAdminUser)

        val result = updateProjectUseCase.execute(updatedProject)

        assertThat(result.exceptionOrNull()).isInstanceOf(NotAuthorizedUserException::class.java)
        verify(exactly = 0) { projectRepository.updateProject(updatedProject) }
        verify(exactly = 1) { userRepository.getUserByUsername(updatedProject.createdBy) }
    }

    @Test
    fun `should update project failed and throw exception, when project is not found for updated project`() {
        val newUpdatedProject = updatedProject.copy(id = "project2")
        every { userRepository.getUserByUsername(newUpdatedProject.createdBy) } returns Result.success(adminUser)
        every { projectRepository.getProjects() } returns Result.success(listOf(fakeProject, updatedProject))

        val result = updateProjectUseCase.execute(newUpdatedProject)

        assertThat(result.exceptionOrNull()).isInstanceOf(NoProjectFoundException::class.java)
        verify(exactly = 0) { projectRepository.updateProject(newUpdatedProject) }
        verify(exactly = 1) { projectRepository.getProjects() }
        verify(exactly = 1) { userRepository.getUserByUsername(newUpdatedProject.createdBy) }
    }


    @Test
    fun `should update project failed and throw exception, when updated project user name is invalid`() {
        val updatedProjectWithInvalidUserName = updatedProject.copy(createdBy = "")
        every { userRepository.getUserByUsername(updatedProjectWithInvalidUserName.createdBy) } returns Result.failure(
            Exception()
        )

        val result = updateProjectUseCase.execute(updatedProjectWithInvalidUserName)

        assertThat(result.exceptionOrNull()).isInstanceOf(InvalidUsernameForProjectException::class.java)
        verify(exactly = 0) { projectRepository.updateProject(updatedProjectWithInvalidUserName) }
        verify(exactly = 0) { userRepository.getUserByUsername(updatedProjectWithInvalidUserName.createdBy) }
    }

    @Test
    fun `should update project failed and throw exception, when updated project name is invalid`() {
        val updatedProjectWithInvalidName = updatedProject.copy(name = "")
        every { userRepository.getUserByUsername(updatedProjectWithInvalidName.createdBy) } returns Result.success(
            adminUser
        )

        val result = updateProjectUseCase.execute(updatedProjectWithInvalidName)

        assertThat(result.exceptionOrNull()).isInstanceOf(InvalidProjectNameException::class.java)
        verify(exactly = 0) { projectRepository.updateProject(updatedProjectWithInvalidName) }
    }
}