package logic.use_cases.project

import helper.project_helper.createProject
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import logic.entities.Project
import logic.entities.User
import logic.entities.UserType
import logic.repositories.AuditRepository
import logic.repositories.ProjectsRepository
import logic.repositories.UserRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

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
        runTest {
            coEvery { userRepository.getUserByUsername(fakeProject.createdBy) } returns adminUser
            coEvery { projectRepository.getProjects() } returns listOf(createProject(id = "dummy1", name = "dummyProject"))
            coEvery { projectRepository.createProject(fakeProject) } returns Unit
            coEvery { auditRepository.createAuditLog(any()) } returns Unit

             createProjectUseCase.execute(fakeProject)

//            assertThat(result.isSuccess).isTrue()
            coVerify(exactly = 1) { projectRepository.createProject(fakeProject) }
            coVerify(exactly = 1) { projectRepository.getProjects() }
            coVerify(exactly = 1) { userRepository.getUserByUsername(fakeProject.createdBy) }
            coVerify(exactly = 1) { auditRepository.createAuditLog(any()) }
        }

    }

    @Test
    fun `should create project failed, when project is already exists`() {
        runTest {
            coEvery { userRepository.getUserByUsername(fakeProject.createdBy) } returns adminUser
            coEvery { projectRepository.getProjects() } returns listOf(fakeProject)
            coEvery { projectRepository.createProject(fakeProject) } returns Unit

            assertThrows<Exception> {   createProjectUseCase.execute(fakeProject)}

            coVerify(exactly = 0) { projectRepository.createProject(fakeProject) }
            coVerify(exactly = 1) { projectRepository.getProjects() }
            coVerify(exactly = 1) { userRepository.getUserByUsername(fakeProject.createdBy) }
        }

    }

    @Test
    fun `should create project failed and throw exception, when user is not admin`() {
        runTest {
            coEvery { userRepository.getUserByUsername(fakeProject.createdBy) } returns mateUser


            assertThrows<Exception> {  createProjectUseCase.execute(fakeProject) }

            coVerify (exactly = 0) { projectRepository.createProject(fakeProject) }
            coVerify(exactly = 1) { userRepository.getUserByUsername(fakeProject.createdBy) }
        }

    }

    @Test
    fun `should create project failed and throw exception, when project user name is invalid`() {
        runTest {
            val fakeProjectWithInvalidUserName = fakeProject.copy(createdBy = "")
            coEvery { userRepository.getUserByUsername(fakeProjectWithInvalidUserName.createdBy) } throws Exception()

            assertThrows<Exception> {  createProjectUseCase.execute(fakeProjectWithInvalidUserName) }

            coVerify(exactly = 0) { projectRepository.createProject(fakeProjectWithInvalidUserName) }
            coVerify(exactly = 0) { userRepository.getUserByUsername(fakeProjectWithInvalidUserName.createdBy) }
        }

    }

    @Test
    fun `should create project failed and throw exception, when project name is invalid`() {
        runTest {
            val fakeProjectWithInvalidName = fakeProject.copy(name = "")
            coEvery { userRepository.getUserByUsername(fakeProjectWithInvalidName.createdBy) } returns adminUser


            assertThrows<Exception> { createProjectUseCase.execute(fakeProjectWithInvalidName) }
            coVerify(exactly = 0) { projectRepository.createProject(fakeProjectWithInvalidName) }
        }
    }

}