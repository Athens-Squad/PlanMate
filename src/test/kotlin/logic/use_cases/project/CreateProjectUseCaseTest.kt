package logic.use_cases.project

import com.google.common.truth.Truth.assertThat
import helper.project_helper.createProject
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import logic.entities.Project
import logic.entities.User
import logic.entities.UserType
import logic.repositories.AuditRepository
import logic.repositories.ProjectsRepository
import logic.repositories.UserRepository
import net.thechance.logic.exceptions.ProjectsLogicExceptions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CreateProjectUseCaseTest {

    private val projectRepository: ProjectsRepository = mockk(relaxed = true)
    private val userRepository: UserRepository = mockk(relaxed = true)

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
            CreateProjectUseCase(projectRepository, userRepository, auditRepository)
    }

    @Test
    fun `should create project successfully, when project is valid`() {
        every { userRepository.getUserByUsername(fakeProject.createdByUserName) } returns Result.success(adminUser)
        every { projectRepository.getProjects() } returns Result.success(listOf(
                createProject(id = "dummy1", name = "dummyProject")
            )
        )
        every { projectRepository.createProject(fakeProject) } returns Result.success(Unit)
        every { auditRepository.createAuditLog(any()) } returns Result.success(Unit)

        val result = createProjectUseCase.execute(fakeProject)

        assertThat(result.isSuccess).isTrue()
        verify(exactly = 1) { projectRepository.createProject(fakeProject) }
        verify(exactly = 1) { projectRepository.getProjects() }
        verify(exactly = 1) { userRepository.getUserByUsername(fakeProject.createdByUserName) }
        verify(exactly = 1) { auditRepository.createAuditLog(any()) }
    }

    @Test
    fun `should create project failed, when project is already exists`() {
        every { userRepository.getUserByUsername(fakeProject.createdByUserName) } returns Result.success(adminUser)
        every { projectRepository.getProjects() } returns Result.success(listOf(fakeProject))
        every { projectRepository.createProject(fakeProject) } returns Result.success(Unit)

        val result = createProjectUseCase.execute(fakeProject)

        assertThat(result.exceptionOrNull()).isInstanceOf(ProjectAlreadyExistException::class.java)
        verify(exactly = 0) { projectRepository.createProject(fakeProject) }
        verify(exactly = 1) { projectRepository.getProjects() }
        verify(exactly = 1) { userRepository.getUserByUsername(fakeProject.createdByUserName) }
    }

    @Test
    fun `should create project failed and throw exception, when user is not admin`() {
        every { userRepository.getUserByUsername(fakeProject.createdByUserName) } returns Result.success(mateUser)

        val result = createProjectUseCase.execute(fakeProject)

        assertThat(result.exceptionOrNull()).isInstanceOf(NotAuthorizedUserException::class.java)
        verify(exactly = 0) { projectRepository.createProject(fakeProject) }
        verify(exactly = 1) { userRepository.getUserByUsername(fakeProject.createdByUserName) }
    }

    @Test
    fun `should create project failed and throw exception, when project user name is invalid`() {
        val fakeProjectWithInvalidUserName = fakeProject.copy(createdByUserName = "")
        every { userRepository.getUserByUsername(fakeProjectWithInvalidUserName.createdByUserName) } returns Result.failure(
            Exception()
        )

        val result = createProjectUseCase.execute(fakeProjectWithInvalidUserName)

        assertThat(result.exceptionOrNull()).isInstanceOf(InvalidUsernameForProjectException::class.java)
        verify(exactly = 0) { projectRepository.createProject(fakeProjectWithInvalidUserName) }
        verify(exactly = 0) { userRepository.getUserByUsername(fakeProjectWithInvalidUserName.createdByUserName) }
    }

    @Test
    fun `should create project failed and throw exception, when project name is invalid`() {
        val fakeProjectWithInvalidName = fakeProject.copy(name = "")
        every { userRepository.getUserByUsername(fakeProjectWithInvalidName.createdByUserName) } returns Result.success(adminUser)

        val result = createProjectUseCase.execute(fakeProjectWithInvalidName)

        assertThat(result.exceptionOrNull()).isInstanceOf(InvalidProjectNameException::class.java)
        verify(exactly = 0) { projectRepository.createProject(fakeProjectWithInvalidName) }
    }

}