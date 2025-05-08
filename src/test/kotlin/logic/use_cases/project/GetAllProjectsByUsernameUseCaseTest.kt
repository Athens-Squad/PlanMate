package logic.use_cases.project

import com.google.common.truth.Truth.assertThat
import helper.project_helper.createProject
import helper.project_helper.fakes.FakeProjectData
import helper.project_helper.fakes.FakeProjectData.adminUser
import helper.project_helper.fakes.FakeProjectData.alexAdminUser
import helper.project_helper.fakes.FakeProjectData.mateUserForAdminUser
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import logic.entities.Project
import logic.repositories.ProjectsRepository
import logic.repositories.UserRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

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
            progressionStates = FakeProjectData.states,
            tasks = FakeProjectData.tasks,
            createdBy = adminUser.name
        )

        adminUserSecondProject = createProject().copy(
            id = "2",
            createdBy = adminUser.name
        )

        alexAdminUserProject = createProject().copy(
            progressionStates = FakeProjectData.states,
            tasks = FakeProjectData.tasks,
            createdBy = alexAdminUser.name
        )
        getAllProjectsByUsernameUseCase = GetAllProjectsByUsernameUseCase(projectRepository, userRepository)
    }

    @Test
    fun `should get all projects by username successfully, when user is admin`() {
        runTest {
            coEvery { userRepository.getUserByUsername(adminUser.name) } returns adminUser
            coEvery { projectRepository.getProjects() } returns listOf(adminUserProject, adminUserSecondProject, alexAdminUserProject)


            val result = getAllProjectsByUsernameUseCase.execute(adminUser.name)
            val expectedResult = listOf(adminUserSecondProject, adminUserProject)

            assertThat(result.containsAll(expectedResult)).isTrue()
            coVerify(exactly = 1) { projectRepository.getProjects() }
            coVerify(exactly = 1) { userRepository.getUserByUsername(adminUser.name) }
        }

    }

    @Test
    fun `should get all projects by username failed and throw exception, when user is not admin`() {
        runTest {
            coEvery { projectRepository.getProjects() } returns listOf(alexAdminUserProject)

            coEvery { userRepository.getUserByUsername(mateUserForAdminUser.name) } returns mateUserForAdminUser

            assertThrows<Exception> {  getAllProjectsByUsernameUseCase.execute(mateUserForAdminUser.name) }

            coVerify (exactly = 0) { projectRepository.getProjects() }
            coVerify(exactly = 1) { userRepository.getUserByUsername(mateUserForAdminUser.name) }
        }

    }

    @Test
    fun `should throw exception, when username is invalid`() {
        runTest {
            val invalidUsername = ""
            coEvery { userRepository.getUserByUsername(invalidUsername) } throws  Exception()

            assertThrows<Exception> { getAllProjectsByUsernameUseCase.execute(invalidUsername) }
            coVerify(exactly = 0) { projectRepository.getProjects() }
            coVerify(exactly = 0) { userRepository.getUserByUsername(invalidUsername) }
        }

    }

    @Test
    fun `should throw exception, when username is not found`() {
        runTest {
            val notExistingUserName = "notExistingUserName"
            coEvery { userRepository.getUserByUsername(notExistingUserName) } throws Exception()

            assertThrows<Exception> { getAllProjectsByUsernameUseCase.execute(notExistingUserName) }
            coVerify(exactly = 0) { projectRepository.getProjects() }
            coVerify(exactly = 1) { userRepository.getUserByUsername(notExistingUserName) }
        }

    }

}