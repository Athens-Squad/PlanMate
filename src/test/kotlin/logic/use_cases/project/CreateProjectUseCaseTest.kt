package logic.use_cases.project

import data.projects.exceptions.ProjectsLogicExceptions
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
import net.thechance.logic.use_cases.project.projectValidations.ProjectValidator
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.math.E

class CreateProjectUseCaseTest {

    private val projectRepository: ProjectsRepository = mockk(relaxed = true)
    private val userRepository: UserRepository = mockk(relaxed = true)

    private val auditRepository: AuditRepository = mockk(relaxed = true)
    private lateinit var fakeProject: Project
    private lateinit var adminUser: User
    private lateinit var mateUser: User
    private lateinit var createProjectUseCase: CreateProjectUseCase
    private  val projectValidator: ProjectValidator = mockk(relaxed = true)

    @BeforeEach
    fun setUp() {
        adminUser = User("1", "admin user", "abc123", UserType.AdminUser)
        mateUser = User("2", "mate user", "123", UserType.MateUser("1"))
        fakeProject = createProject().copy(id = "1")
        createProjectUseCase = CreateProjectUseCase(projectRepository, userRepository, auditRepository,projectValidator)
    }

        @Test
    fun `should create project failed and throw exception, when project create by is invalid`() {
        runTest {
            val fakeProjectWithInvalidCreateBy = fakeProject.copy(createdBy = "", name = "projectName")
            coEvery { projectValidator.checkIfValueIsBlank(any()) } returns true

            assertThrows<ProjectsLogicExceptions.InvalidProjectNameException> {  createProjectUseCase.execute(fakeProjectWithInvalidCreateBy) }
        }

    }

    @Test
    fun `should create project failed and throw exception, when project name is invalid`() {
        runTest {
            val fakeProjectWithInvalidProjectName = fakeProject.copy(createdBy = "mohamed", name = "")
            coEvery { projectValidator.checkIfValueIsBlank(fakeProjectWithInvalidProjectName.name) } returns true


            assertThrows<ProjectsLogicExceptions.InvalidProjectNameException> { createProjectUseCase.execute(fakeProjectWithInvalidProjectName) }
        }
    }

    @Test
    fun `should create project failed and throw exception, get user by user name throw exception`() {
        runTest {
            coEvery {userRepository.getUserByUsername(adminUser.name)  } throws Exception()

            assertThrows<Exception> { createProjectUseCase.execute(fakeProject) }
        }
    }

    @Test
    fun `should create project failed and throw exception, when user is not admin`() {
        runTest {
            coEvery { userRepository.getUserByUsername(fakeProject.createdBy) } returns mateUser

            assertThrows<Exception> {  createProjectUseCase.execute(fakeProject) }

        }

    }

    @Test
    fun `should create project failed, when get projects throw exception`() {
        runTest {
            val projects = listOf(fakeProject)
            coEvery { projectValidator.checkIfValueIsBlank(any()) } returns false
            coEvery { userRepository.getUserByUsername(adminUser.name) } returns adminUser
            coEvery { projectRepository.getProjects() } throws Exception()
            coEvery { projectValidator.checkIfProjectAlreadyExistInRepository(fakeProject.id,projects) }

            assertThrows<Exception> {   createProjectUseCase.execute(fakeProject)}
        }

    }

    @Test
    fun `should create project failed, when project is already exists`() {
        runTest {
            val projects = listOf(fakeProject)
            coEvery { projectValidator.checkIfValueIsBlank(any()) } returns false
            coEvery { userRepository.getUserByUsername(adminUser.name) } returns adminUser
            coEvery { projectRepository.getProjects() } returns projects
            coEvery { projectValidator.checkIfProjectAlreadyExistInRepository(fakeProject.id,projects) } returns true

            assertThrows<Exception> {   createProjectUseCase.execute(fakeProject)}
        }

    }


    @Test
    fun `should create project successfully, when project is valid`() {
        runTest {
            val project = fakeProject.copy(createdBy = "mohamed ragab" , name = "project")
            val projects = listOf(fakeProject.copy(id = "4"))
            coEvery { projectValidator.checkIfValueIsBlank(any()) } returns false
            coEvery { projectValidator.checkIfValueIsBlank(project.name) } returns false
            coEvery { userRepository.getUserByUsername(adminUser.name) } returns adminUser
            coEvery { projectValidator.checkIfUserIsAdmin(any()) } returns true
            coEvery { projectRepository.getProjects() } returns projects
            coEvery { projectValidator.checkIfProjectAlreadyExistInRepository(any(),any()) }returns false
            coEvery { projectRepository.createProject(any()) }  returns Unit

             createProjectUseCase.execute(project)

            coVerify(exactly = 1) { projectRepository.createProject(project) }
            coVerify(exactly = 1) { projectRepository.getProjects() }
            coVerify(exactly = 1) { userRepository.getUserByUsername(project.createdBy) }
            coVerify(exactly = 1) { auditRepository.createAuditLog(any()) }
        }
    }

}