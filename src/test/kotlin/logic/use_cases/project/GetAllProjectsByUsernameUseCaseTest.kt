package logic.use_cases.project

import com.google.common.truth.Truth.assertThat
import helper.project_helper.createProject
import helper.project_helper.fakes.FakeProjectData
import helper.project_helper.fakes.FakeProjectData.adminUser
import helper.project_helper.fakes.FakeProjectData.alexAdminUser
import helper.project_helper.fakes.FakeProjectData.mateUserForAdminUser
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import logic.entities.Project
import logic.repositories.ProjectsRepository
import logic.repositories.UserRepository
import logic.use_cases.project.exceptions.ProjectsLogicExceptions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetAllProjectsByUsernameUseCaseTest {
    
    private val projectRepository: ProjectsRepository = mockk(relaxed = true)
    private val userRepository: UserRepository = mockk(relaxed = true)

    private lateinit var adminUserProject: Project
    private lateinit var adminUserSecondProject: Project
    private lateinit var alexAdminUserProject: Project

    private lateinit var getAllProjectsByUsernameUseCase: GetAllProjectsByUsernameUseCase
    
    @BeforeEach
    fun setUp() {
        adminUserProject = createProject().copy(
            id = "1",
            states = FakeProjectData.states,
            tasks = FakeProjectData.tasks,
            createdBy = adminUser.name
        )

        adminUserSecondProject = createProject().copy(
            id = "2",
            createdBy = adminUser.name
        )

        alexAdminUserProject = createProject().copy(
            states = FakeProjectData.states,
            tasks = FakeProjectData.tasks,
            createdBy = alexAdminUser.name
        )
        getAllProjectsByUsernameUseCase = GetAllProjectsByUsernameUseCase(projectRepository, userRepository)
    }

    @Test
    fun `should get all projects by username successfully, when user is admin`() {
        every { userRepository.getUserByUsername(adminUser.name) } returns Result.success(adminUser)
        every { projectRepository.getProjects() } returns Result.success(
            listOf(adminUserProject, adminUserSecondProject, alexAdminUserProject)
        )

        val result = getAllProjectsByUsernameUseCase.execute(adminUser.name)
        val expectedResult = listOf(adminUserSecondProject, adminUserProject)

        assertThat(result.getOrThrow()).containsExactlyElementsIn(expectedResult)
        verify(exactly = 1) { projectRepository.getProjects() }
        verify(exactly = 1) { userRepository.getUserByUsername(adminUser.name) }
    }

    @Test
    fun `should get all projects by username failed and throw exception, when user is not admin`() {
        every { projectRepository.getProjects() } returns Result.success(
            listOf(alexAdminUserProject)
        )
        every { userRepository.getUserByUsername(mateUserForAdminUser.name) } returns Result.success(mateUserForAdminUser)

        val result = getAllProjectsByUsernameUseCase.execute(mateUserForAdminUser.name)

        assertThat(result.exceptionOrNull()).isInstanceOf(NotAuthorizedUserException::class.java)
        verify(exactly = 0) { projectRepository.getProjects() }
        verify(exactly = 1) { userRepository.getUserByUsername(mateUserForAdminUser.name) }
    }

    @Test
    fun `should throw exception, when username is invalid`() {
        val invalidUsername = ""
        every { userRepository.getUserByUsername(invalidUsername) } returns Result.failure(Exception())

        val result = getAllProjectsByUsernameUseCase.execute(invalidUsername)

        assertThat(result.exceptionOrNull()).isInstanceOf(InvalidUsernameForProjectException::class.java)
        verify(exactly = 0) { projectRepository.getProjects() }
        verify(exactly = 0) { userRepository.getUserByUsername(invalidUsername) }
    }

    @Test
    fun `should throw exception, when username is not found`() {
        val notExistingUserName = "notExistingUserName"
        every { userRepository.getUserByUsername(notExistingUserName) } returns Result.failure(Exception())

        val result = getAllProjectsByUsernameUseCase.execute(notExistingUserName)

        assertThat(result.exceptionOrNull()).isInstanceOf(NotAuthorizedUserException::class.java)
        verify(exactly = 0) { projectRepository.getProjects() }
        verify(exactly = 1) { userRepository.getUserByUsername(notExistingUserName) }
    }

}