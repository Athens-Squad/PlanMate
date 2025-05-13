package logic.use_cases.project

import helper.project_helper.createProject
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import logic.entities.Project
import logic.repositories.ProjectsRepository
import net.thechance.logic.exceptions.NotAuthorizedUserException
import net.thechance.logic.validators.projectValidations.ProjectValidator
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.uuid.ExperimentalUuidApi

class GetAllProjectsByUsernameUseCaseTest {

    private lateinit var getAllProjectsByUsernameUseCase: GetAllProjectsByUsernameUseCase
    private val projectValidator : ProjectValidator = mockk(relaxed = true)
    private val projectRepository: ProjectsRepository = mockk(relaxed = true)

    private lateinit var adminUserProject: Project
    private lateinit var adminUserSecondProject: Project
    private lateinit var alexAdminUserProject: Project


    @OptIn(ExperimentalUuidApi::class)
    @BeforeEach
    fun setUp() {
        adminUserProject = createProject().copy(
            name = "project1"
        )

        adminUserSecondProject = createProject().copy(
            createdByUserName = "mohamed"
        )

        alexAdminUserProject = createProject().copy(
            createdByUserName = "hallo"
        )
        getAllProjectsByUsernameUseCase = GetAllProjectsByUsernameUseCase(
            projectRepository = projectRepository,
            projectValidator = projectValidator,)
    }

    @Test
    fun `should get all projects by username failed and throw exception, when user is not admin`(){
        runTest {
            val userNameNotAdmin = "Mohamed"
            coEvery {projectValidator.validateUserIsAuthorized(userNameNotAdmin)} throws NotAuthorizedUserException()

            assertThrows<NotAuthorizedUserException> { getAllProjectsByUsernameUseCase.execute(userNameNotAdmin) }
        }
    }
    @Test
    fun `should get all projects by username successfully, when user is admin`() {
        runTest {
            val userNameAdmin = "ali"
            coEvery {projectValidator.validateUserIsAuthorized(userNameAdmin)} returns true
            getAllProjectsByUsernameUseCase.execute(userNameAdmin)
            coVerify { projectRepository.getProjects() }
        }
    }
}