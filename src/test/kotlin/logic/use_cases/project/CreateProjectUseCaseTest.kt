package logic.use_cases.project

import helper.project_helper.createProject
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import logic.entities.Project
import logic.entities.User
import logic.entities.UserType
import logic.repositories.ProjectsRepository
import logic.repositories.UserRepository
import logic.use_cases.audit_log.CreateAuditLogUseCase
import net.thechance.logic.exceptions.InvalidProjectFieldsException
import net.thechance.logic.exceptions.ProjectAlreadyExistException
import net.thechance.logic.use_cases.project.projectValidations.ProjectValidator
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.uuid.ExperimentalUuidApi

class CreateProjectUseCaseTest {

    private val projectRepository: ProjectsRepository = mockk(relaxed = true)
    private val userRepository: UserRepository = mockk(relaxed = true)

    private val auditRepository: CreateAuditLogUseCase = mockk(relaxed = true)
    private lateinit var fakeProject: Project
    private lateinit var adminUser: User
    private lateinit var mateUser: User
    private lateinit var createProjectUseCase: CreateProjectUseCase
    private val projectValidator: ProjectValidator = mockk(relaxed = true)

    @OptIn(ExperimentalUuidApi::class)
    @BeforeEach
    fun setUp() {
        adminUser = User(name = "admin user", type = UserType.AdminUser)
        mateUser = User(name = "mate user", type = UserType.MateUser("1"))
        fakeProject = createProject()
        createProjectUseCase =
            CreateProjectUseCase(
                projectRepository = projectRepository,
                projectValidator = projectValidator,
                createAuditLogUseCase = auditRepository
            )
    }


    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun `should create project failed and throw exception, when project name is invalid`() {
        runTest {
            val fakeProjectWithInvalidProjectName = fakeProject.copy(name = "")
            coEvery { projectValidator.validateProjectFieldsNotBlank(fakeProjectWithInvalidProjectName) } throws InvalidProjectFieldsException()

            assertThrows<InvalidProjectFieldsException> { createProjectUseCase.execute(fakeProjectWithInvalidProjectName) }
        }

    }


    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun `should create project failed and throw exception, when project created by user name is invalid`() {
        runTest {
            val fakeProjectWithInvalidCreatedByUserName = fakeProject.copy(createdByUserName = "")
            every { projectValidator.validateProjectFieldsNotBlank(fakeProjectWithInvalidCreatedByUserName) } throws InvalidProjectFieldsException()


            assertThrows<InvalidProjectFieldsException> {
                createProjectUseCase.execute(
                    fakeProjectWithInvalidCreatedByUserName
                )
            }
        }

    }

    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun `should create project failed and throw exception, when user is not admin`() {
        runTest {
            val fakeProjectWithMateUser = fakeProject.copy(createdByUserName = mateUser.name)

            coEvery { projectValidator.validateUserIsAuthorized(fakeProjectWithMateUser.createdByUserName) } throws InvalidProjectFieldsException()


            assertThrows<InvalidProjectFieldsException> { createProjectUseCase.execute(fakeProjectWithMateUser) }
        }

    }

    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun `should create project failed, when project is already exists`() {
        runTest {
            val fakeProjectAlreadyExist = fakeProject
            coEvery { projectValidator.validateProjectNotExists(fakeProjectAlreadyExist.id) } throws ProjectAlreadyExistException()

            assertThrows<ProjectAlreadyExistException> { createProjectUseCase.execute(fakeProjectAlreadyExist) }
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun `should create project successfully, when project is valid`() {
        runTest {
            val validProject = fakeProject
            every { projectValidator.validateProjectFieldsNotBlank(any()) } returns true
            coEvery { projectValidator.validateUserIsAuthorized(any()) } returns true
            coEvery { projectValidator.validateProjectNotExists(any()) } returns true

            createProjectUseCase.execute(validProject)

            coVerify { projectRepository.createProject(validProject) }

        }

    }

}