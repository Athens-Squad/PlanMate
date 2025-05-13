package logic.use_cases.project

import helper.project_helper.createProject
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import logic.entities.Project
import logic.entities.User
import logic.entities.UserType
import logic.repositories.ProjectsRepository
import logic.use_cases.audit_log.CreateAuditLogUseCase
import net.thechance.logic.exceptions.InvalidUsernameForProjectException
import net.thechance.logic.exceptions.NoProjectFoundException
import net.thechance.logic.exceptions.NotAuthorizedUserException
import net.thechance.logic.validators.projectValidations.ProjectValidator
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test
import kotlin.uuid.ExperimentalUuidApi

class DeleteProjectUseCaseTest {

    private lateinit var deleteProjectUseCase: DeleteProjectUseCase
    private val projectRepository: ProjectsRepository = mockk(relaxed = true)
    private val projectValidator : ProjectValidator = mockk(relaxed = true )
    private val createAuditLogUseCase: CreateAuditLogUseCase = mockk(relaxed = true)
    private lateinit var fakeProject: Project
    private lateinit var adminUser: User
    private lateinit var mateUser: User



    @OptIn(ExperimentalUuidApi::class)
    @BeforeEach
    fun setUp() {
        fakeProject = createProject()
        adminUser = User(name = "admin user", type = UserType.AdminUser)
        mateUser = User(name = "mate user", type = UserType.MateUser("1"))

        deleteProjectUseCase = DeleteProjectUseCase(
            projectRepository=projectRepository,
            projectValidator = projectValidator,
            createAuditLogUseCase = createAuditLogUseCase
        )
    }


    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun `should delete project failed and throw exception, when username is invalid`() {
        runTest {
            val invalidAdminUserName = ""
            coEvery { projectValidator.validateUserIsAuthorized(invalidAdminUserName) }  throws InvalidUsernameForProjectException()

            assertThrows < InvalidUsernameForProjectException>{deleteProjectUseCase.execute(fakeProject.id , invalidAdminUserName)  }
        }

    }


    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun `should delete project failed and throw exception, when user is not admin`() {
        runTest {
            coEvery { projectValidator.validateUserIsAuthorized(mateUser.name) }  throws NotAuthorizedUserException()

            assertThrows < NotAuthorizedUserException> {deleteProjectUseCase.execute(fakeProject.id,mateUser.name)}
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun `should delete project failed and throw exception, when project not found for deleted project`() {
        runTest {
            coEvery { projectValidator.validateProjectAlreadyExists(fakeProject.id) } throws NoProjectFoundException()

            assertThrows < NoProjectFoundException> {deleteProjectUseCase.execute(fakeProject.id,adminUser.name)}
        }

    }

    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun `should delete project failed, when user is not the owner of project`() {
        runTest {
            coEvery { projectValidator.validateUserIsTheProjectOwner(fakeProject.id , adminUser.name) } throws Exception()

            assertThrows < Exception>{ deleteProjectUseCase.execute(fakeProject.id,adminUser.name) }
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun `should delete project successfully, when project is valid`() {
        runTest {
            coEvery { projectValidator.validateUserIsAuthorized(any()) } returns true
            coEvery { projectValidator.validateProjectAlreadyExists(any()) } returns true
            coEvery { projectValidator.validateUserIsTheProjectOwner(any() , any()) }returns true

            deleteProjectUseCase.execute(fakeProject.id , adminUser.name)

            coVerify { projectRepository.deleteProject(fakeProject.id) }
        }

    }



}