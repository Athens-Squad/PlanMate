package logic.use_cases.project

import com.google.common.truth.Truth.assertThat
import helper.project_helper.createProject
import helper.project_helper.fakes.FakeProjectData
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import logic.entities.Project
import logic.entities.User
import logic.repositories.*
import net.thechance.logic.entities.UserType
import net.thechance.logic.use_cases.project.CreateProjectUseCase
import net.thechance.logic.use_cases.project.exceptions.ProjectsLogicExceptions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class CreateProjectUseCaseTest {

    private val projectRepository: ProjectsRepository = mockk(relaxed = true)
    private val userRepository: UserRepository = mockk(relaxed = true)
    private val statesRepository: StatesRepository = mockk(relaxed = true)
    private val tasksRepository: TasksRepository = mockk(relaxed = true)

    private val auditRepository: AuditRepository = mockk(relaxed = true)
    private lateinit var fakeProject: Project
    private lateinit var adminUser: User
    private lateinit var mateUser: User
    private lateinit var createProjectUseCase: CreateProjectUseCase

    @BeforeEach
    fun setUp() {
        adminUser = User("1", "admin user", "abc123", UserType.AdminUser)
        mateUser = User("2", "mate user", "123", UserType.MateUser("1"))
        fakeProject = createProject().copy(id = "1")
        createProjectUseCase =
            CreateProjectUseCase(projectRepository, statesRepository, tasksRepository, userRepository, auditRepository)
    }

    @Test
    fun `should create project successfully, when project is valid`() {
        every { userRepository.getUserByUsername(fakeProject.createdBy) } returns Result.success(adminUser)
        every { tasksRepository.getTasksByProjectId(fakeProject.id) } returns Result.success(FakeProjectData.tasks)
        every { statesRepository.getStates() } returns Result.success(FakeProjectData.states)

        every { projectRepository.createProject(fakeProject) } returns Result.success(Unit)
        every { auditRepository.createAuditLog(any()) } returns Result.success(Unit)

        val result = createProjectUseCase.execute(fakeProject)

        assertThat(result.isSuccess).isTrue()
        verify(exactly = 1) { projectRepository.createProject(fakeProject) }
        verify(exactly = 1) { userRepository.getUserByUsername(fakeProject.createdBy) }
        verify(exactly = 1) { auditRepository.createAuditLog(any()) }
    }

    @Test
    fun `should create project failed and throw exception, when user is not admin`() {
        every { userRepository.getUserByUsername(fakeProject.createdBy) } returns Result.success(mateUser)

        val result = createProjectUseCase.execute(fakeProject)

        assertThat(result.exceptionOrNull()).isInstanceOf(NotAuthorizedUserException::class.java)
        verify(exactly = 0) { projectRepository.createProject(fakeProject) }
        verify(exactly = 1) { userRepository.getUserByUsername(fakeProject.createdBy) }
    }

    @Test
    fun `should create project failed and throw exception, when project user name is invalid`() {
        val fakeProjectWithInvalidUserName = fakeProject.copy(createdBy = "")
        every { userRepository.getUserByUsername(fakeProjectWithInvalidUserName.createdBy) } returns Result.failure(
            Exception()
        )

        val result = createProjectUseCase.execute(fakeProjectWithInvalidUserName)

        assertThat(result.exceptionOrNull()).isInstanceOf(InvalidUsernameForProjectException::class.java)
        verify(exactly = 0) { projectRepository.createProject(fakeProjectWithInvalidUserName) }
        verify(exactly = 0) { userRepository.getUserByUsername(fakeProjectWithInvalidUserName.createdBy) }
    }

    @Test
    fun `should create project failed and throw exception, when project name is invalid`() {
        val fakeProjectWithInvalidName = fakeProject.copy(name = "")
        every { userRepository.getUserByUsername(fakeProjectWithInvalidName.createdBy) } returns Result.success(adminUser)

        val result = createProjectUseCase.execute(fakeProjectWithInvalidName)

        assertThat(result.exceptionOrNull()).isInstanceOf(InvalidProjectNameException::class.java)
        verify(exactly = 0) { projectRepository.createProject(fakeProjectWithInvalidName) }
    }

    @Test
    fun `should create project failed and throw exception, when project states are invalid`() {
        val fakeProjectWithInvalidStates = fakeProject.copy(states = mutableListOf())
        every { tasksRepository.getTasksByProjectId(fakeProjectWithInvalidStates.id) } returns Result.success(
            FakeProjectData.tasks
        )
        every { statesRepository.getStates() } returns Result.failure(Exception())
        every { userRepository.getUserByUsername(fakeProjectWithInvalidStates.createdBy) } returns Result.success(
            adminUser
        )

        assertThrows<NoStatesFoundForProjectException> {
            createProjectUseCase.execute(fakeProjectWithInvalidStates).getOrThrow()
        }
        verify(exactly = 0) { projectRepository.createProject(fakeProjectWithInvalidStates) }
        verify(exactly = 1) { statesRepository.getStates() }
    }

    @Test
    fun `should create project failed and throw exception, when project tasks are invalid`() {
        val fakeProjectWithInvalidTasks = fakeProject.copy(id = "1", tasks = mutableListOf())
        every { statesRepository.getStates() } returns Result.success(FakeProjectData.states)
        every { tasksRepository.getTasksByProjectId(fakeProjectWithInvalidTasks.id) } returns Result.failure(Exception())
        every { userRepository.getUserByUsername(fakeProjectWithInvalidTasks.createdBy) } returns Result.success(
            adminUser
        )

        assertThrows<NoTasksFoundForProjectException> {
            createProjectUseCase.execute(fakeProjectWithInvalidTasks).getOrThrow()
        }
        verify(exactly = 0) { projectRepository.createProject(fakeProjectWithInvalidTasks) }
        verify(exactly = 1) { tasksRepository.getTasksByProjectId(fakeProjectWithInvalidTasks.id) }
    }

}