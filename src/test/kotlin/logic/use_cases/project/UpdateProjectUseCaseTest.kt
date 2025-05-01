package logic.use_cases.project

import com.google.common.truth.Truth.assertThat
import helper.project_helper.createProject
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import logic.entities.Project
import logic.entities.Task
import logic.entities.User
import logic.repositories.*
import net.thechance.logic.entities.State
import net.thechance.logic.entities.UserType
import net.thechance.logic.use_cases.project.UpdateProjectUseCase
import net.thechance.logic.use_cases.project.exceptions.ProjectsLogicExceptions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class UpdateProjectUseCaseTest {

    private val projectRepository: ProjectsRepository = mockk(relaxed = true)
    private val userRepository: UserRepository = mockk(relaxed = true)
    private val statesRepository: StatesRepository = mockk(relaxed = true)
    private val tasksRepository: TasksRepository = mockk(relaxed = true)

    private val auditRepository: AuditRepository = mockk(relaxed = true)
    private lateinit var fakeProject: Project
    private lateinit var updatedProject: Project
    private lateinit var adminUser: User
    private lateinit var mateUser: User
    private lateinit var updateProjectUseCase: UpdateProjectUseCase

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
        updatedProject = fakeProject.copy(
            name = "Updated Project",
            description = "Just a simple updated project",
        )

        updateProjectUseCase =
            UpdateProjectUseCase(projectRepository, statesRepository, tasksRepository, userRepository, auditRepository)

    }

    @Test
    fun `should update project successfully, when update is valid`() {
        every { projectRepository.getProjects() } returns Result.success(listOf(fakeProject))
        every { projectRepository.updateProject(updatedProject) } returns Result.success(Unit)
        every { userRepository.getUserByUsername(updatedProject.createdBy) } returns Result.success(adminUser)
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
        every { userRepository.getUserByUsername(updatedProject.createdBy) } returns Result.success(mateUser)

        val result = updateProjectUseCase.execute(updatedProject)

        assertThat(result.exceptionOrNull()).isInstanceOf(NotAuthorizedUserException::class.java)
        verify(exactly = 0) { projectRepository.updateProject(updatedProject) }
        verify(exactly = 1) { userRepository.getUserByUsername(updatedProject.createdBy) }
    }

    @Test
    fun `should update project failed and throw exception, when project is not found for updated project`() {
        val newUpdatedProject = updatedProject.copy(id = "project2")
        every { userRepository.getUserByUsername(newUpdatedProject.createdBy) } returns Result.success(adminUser)
        every { projectRepository.getProjects() } returns Result.failure(Exception())

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

        assertThat(result.exceptionOrNull()).isInstanceOf(NoUserFoundForProjectException::class.java)
        verify(exactly = 0) { projectRepository.updateProject(updatedProjectWithInvalidUserName) }
        verify(exactly = 0) { userRepository.getUserByUsername(updatedProjectWithInvalidUserName.createdBy) }
    }

    @Test
    fun `should update project failed and throw exception, when updated project name is invalid`() {
        val updatedProjectWithInvalidName = updatedProject.copy(name = "")
        every { userRepository.getUserByUsername(updatedProjectWithInvalidName.createdBy) } returns Result.success(adminUser)

        val result = updateProjectUseCase.execute(updatedProjectWithInvalidName)

        assertThat(result.exceptionOrNull()).isInstanceOf(InvalidProjectNameException::class.java)
        verify(exactly = 0) { projectRepository.updateProject(updatedProjectWithInvalidName) }
    }

    @Test
    fun `should update project failed and throw exception, when updated project states are invalid`() {
        val updatedProjectWithInvalidStates = updatedProject.copy(states = mutableListOf())
        every { statesRepository.getStates() } returns Result.failure(Exception())
        every { userRepository.getUserByUsername(updatedProjectWithInvalidStates.createdBy) } returns Result.success(
            adminUser
        )

        val result = updateProjectUseCase.execute(updatedProjectWithInvalidStates)

        assertThat(result.exceptionOrNull()).isInstanceOf(NoStatesFoundForProjectException::class.java)
        verify(exactly = 0) { projectRepository.updateProject(updatedProjectWithInvalidStates) }
        verify(exactly = 1) { statesRepository.getStates() }
    }

    @Test
    fun `should update project failed and throw exception, when  updated project tasks are invalid`() {
        val updatedProjectWithInvalidTasks = updatedProject.copy(tasks = mutableListOf())
        every { tasksRepository.getTasksByProjectId(updatedProjectWithInvalidTasks.id) } returns Result.failure(Exception())
        every { userRepository.getUserByUsername(updatedProjectWithInvalidTasks.createdBy) } returns Result.success(
            adminUser
        )

        val result = updateProjectUseCase.execute(updatedProjectWithInvalidTasks)

        assertThat(result.exceptionOrNull()).isInstanceOf(NoTasksFoundForProjectException::class.java)
        verify(exactly = 0) { projectRepository.updateProject(updatedProjectWithInvalidTasks) }
        verify(exactly = 1) { tasksRepository.getTasksByProjectId(updatedProjectWithInvalidTasks.id) }
    }
}