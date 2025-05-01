package logic.use_cases.project

import com.google.common.truth.Truth.assertThat
import helper.project_helper.createProject
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import logic.entities.Project
import logic.entities.Task
import logic.entities.User
import logic.repositories.AuditRepository
import logic.repositories.ProjectsRepository
import logic.repositories.UserRepository
import net.thechance.logic.entities.State
import net.thechance.logic.entities.UserType
import net.thechance.logic.use_cases.project.DeleteProjectUseCase
import net.thechance.logic.use_cases.project.exceptions.ProjectsLogicExceptions.NoProjectFoundException
import net.thechance.logic.use_cases.project.exceptions.ProjectsLogicExceptions.NotAuthorizedUserException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class DeleteProjectUseCaseTest {
    private val projectRepository: ProjectsRepository = mockk(relaxed = true)
    private val userRepository: UserRepository = mockk(relaxed = true)

    private val auditRepository: AuditRepository = mockk(relaxed = true)
    private lateinit var fakeProject: Project
    private lateinit var adminUser: User
    private lateinit var mateUser: User

    private lateinit var deleteProjectUseCase: DeleteProjectUseCase

    @BeforeEach
    fun setUp() {
        adminUser = User("1", "admin user", "abc123", UserType.AdminUser)
        mateUser = User("2", "mate user", "123", UserType.MateUser("1"))

        val projectId = "project1"
        val firstState = State("1", "state1", projectId)
        val secondState = State("2", "state2", projectId)
        val firstTask = Task("1", "task1", "task des", firstState, projectId)
        val secondTask = Task("2", "task2", "task des", secondState, projectId)
        fakeProject = createProject().copy(
            states = mutableListOf(firstState, secondState),
            tasks = mutableListOf(firstTask, secondTask)
        )

        deleteProjectUseCase = DeleteProjectUseCase(projectRepository, userRepository, auditRepository)
    }

    @Test
    fun `should delete project successfully, when project is valid`() {
        every { projectRepository.getProjects() } returns Result.success(listOf(fakeProject))
        every { projectRepository.updateProject(fakeProject) } returns Result.success(Unit)
        every { userRepository.getUserByUsername(fakeProject.createdBy) } returns Result.success(adminUser)
        every { auditRepository.createAuditLog(any()) } returns Result.success(Unit)

        deleteProjectUseCase.execute(fakeProject.id)
        val result = projectRepository.getProjects()

        assertThat(result.getOrDefault(emptyList())).isEmpty()
        verify(exactly = 1) { projectRepository.deleteProject(fakeProject.id) }
        verify(exactly = 1) { projectRepository.getProjects() }
        verify(exactly = 1) { userRepository.getUserByUsername(fakeProject.createdBy) }
        verify(exactly = 1) { auditRepository.createAuditLog(any()) }
    }

    @Test
    fun `should delete project failed and throw exception, when user is not admin`() {
        every { userRepository.getUserByUsername(fakeProject.createdBy) } returns Result.success(mateUser)

        val result = deleteProjectUseCase.execute(fakeProject.id)

        assertThat(result.exceptionOrNull()).isInstanceOf(NotAuthorizedUserException::class.java)
        verify(exactly = 0) { projectRepository.deleteProject(fakeProject.id) }
        verify(exactly = 1) { userRepository.getUserByUsername(fakeProject.createdBy) }
    }

    @Test
    fun `should delete project failed and throw exception, when project not found for deleted project`() {
        val notExistingProjectId = "projectIdDoNotExist"
        every { userRepository.getUserByUsername(fakeProject.createdBy) } returns Result.success(adminUser)
        every { projectRepository.getProjects() } returns Result.failure(Exception())

        val result = deleteProjectUseCase.execute(notExistingProjectId)

        assertThat(result.exceptionOrNull()).isInstanceOf(NoProjectFoundException::class.java)
        verify(exactly = 0) { projectRepository.deleteProject(notExistingProjectId) }
        verify(exactly = 1) { projectRepository.getProjects() }
        verify(exactly = 1) { userRepository.getUserByUsername(fakeProject.createdBy) }
    }

    @Test
    fun `should delete project failed and throw exception, when project id is invalid`() {
        val invalidProjectId = ""
        every { userRepository.getUserByUsername(fakeProject.createdBy) } returns Result.success(adminUser)
        every { projectRepository.getProjects() } returns Result.failure(Exception())

        val result = deleteProjectUseCase.execute(invalidProjectId)

        assertThat(result.exceptionOrNull()).isInstanceOf(NoProjectFoundException::class.java)
        verify(exactly = 0) { projectRepository.deleteProject(invalidProjectId) }
        verify(exactly = 1) { projectRepository.getProjects() }
        verify(exactly = 1) { userRepository.getUserByUsername(fakeProject.createdBy) }
    }
}