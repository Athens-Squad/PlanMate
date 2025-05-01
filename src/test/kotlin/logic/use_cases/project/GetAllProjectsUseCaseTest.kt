package logic.use_cases.project

import com.google.common.truth.Truth.assertThat
import helper.project_helper.createProject
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import logic.entities.Project
import logic.entities.Task
import logic.entities.User
import logic.repositories.ProjectsRepository
import logic.repositories.UserRepository
import net.thechance.logic.entities.State
import net.thechance.logic.entities.UserType
import net.thechance.logic.use_cases.project.GetAllProjectsUseCase
import net.thechance.logic.use_cases.project.exceptions.ProjectsLogicExceptions.NoUserFoundForProjectException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetAllProjectsUseCaseTest {

    private val projectRepository: ProjectsRepository = mockk(relaxed = true)
    private val userRepository: UserRepository = mockk(relaxed = true)

    private lateinit var fakeProjectVisibleForAllUsers: Project
    private lateinit var fakeProjectVisibleForAdminsOnly: Project
    private lateinit var adminUser: User
    private lateinit var secondAdminUser: User
    private lateinit var mateUser: User

    private lateinit var getAllProjectsUseCase: GetAllProjectsUseCase

    @BeforeEach
    fun setUp() {
        adminUser = User("1", "admin user", "abc123", UserType.AdminUser)
        mateUser = User("2", "mate user", "123", UserType.MateUser("1"))

        secondAdminUser = User("3", "second admin user", "1232#$", UserType.AdminUser)

        val projectId = "project1"
        val firstState = State("1", "state1", projectId)
        val secondState = State("2", "state2", projectId)
        val firstTask = Task("1", "task1", "task des", firstState, projectId)
        val secondTask = Task("2", "task2", "task des", secondState, projectId)
        fakeProjectVisibleForAllUsers = createProject().copy(
            states = mutableListOf(firstState, secondState),
            tasks = mutableListOf(firstTask, secondTask),
            createdBy = adminUser.name
        )

        fakeProjectVisibleForAdminsOnly = createProject().copy(
            states = mutableListOf(firstState, secondState),
            tasks = mutableListOf(firstTask, secondTask),
            createdBy = secondAdminUser.name
        )
        getAllProjectsUseCase = GetAllProjectsUseCase(projectRepository, userRepository)
    }


    @Test
    fun `should get all projects successfully, when user is admin`() {
        every { projectRepository.getProjects() } returns Result.success(
            listOf(fakeProjectVisibleForAllUsers, fakeProjectVisibleForAdminsOnly)
        )
        every { userRepository.getUserByUsername(adminUser.name) } returns Result.success(adminUser)

        val result = getAllProjectsUseCase.execute(adminUser.name)
        val expectedResult = listOf(fakeProjectVisibleForAllUsers, fakeProjectVisibleForAdminsOnly)

        assertThat(result.getOrDefault(emptyList())).containsExactly(expectedResult)
        verify(exactly = 1) { projectRepository.getProjects() }
        verify(exactly = 1) { userRepository.getUserByUsername(adminUser.name) }
    }

    @Test
    fun `should get only relevant projects successfully, when user is mate`() {
        every { projectRepository.getProjects() } returns Result.success(
            listOf(fakeProjectVisibleForAllUsers, fakeProjectVisibleForAdminsOnly)
        )
        every { userRepository.getUserByUsername(mateUser.name) } returns Result.success(mateUser)

        val result = getAllProjectsUseCase.execute(mateUser.name)
        val expectedResult = listOf(fakeProjectVisibleForAllUsers)

        assertThat(result.getOrDefault(emptyList())).containsExactly(expectedResult)
        verify(exactly = 1) { projectRepository.getProjects() }
        verify(exactly = 1) { userRepository.getUserByUsername(mateUser.name) }
    }

    @Test
    fun `should returns empty list, when no relevant projects exist for mate user`() {
        every { projectRepository.getProjects() } returns Result.success(
            listOf(fakeProjectVisibleForAdminsOnly)
        )
        every { userRepository.getUserByUsername(mateUser.name) } returns Result.success(mateUser)

        val result = getAllProjectsUseCase.execute(mateUser.name)

        assertThat(result.getOrNull()).isEmpty()
        verify(exactly = 0) { projectRepository.getProjects() }
        verify(exactly = 1) { userRepository.getUserByUsername(mateUser.name) }
    }

    @Test
    fun `should throw exception, when username not found`() {
        val invalidUserName = "userNameDoesNotExist"
        every { userRepository.getUserByUsername(invalidUserName) } returns Result.failure(Exception())

        val result = getAllProjectsUseCase.execute(invalidUserName)

        assertThat(result.exceptionOrNull()).isInstanceOf(NoUserFoundForProjectException::class.java)
        verify(exactly = 0) { projectRepository.getProjects() }
        verify(exactly = 1) { userRepository.getUserByUsername(invalidUserName) }
    }

    @Test
    fun `should throw exception, when username is invalid `() {
        val invalidUserName = ""
        every { userRepository.getUserByUsername(invalidUserName) } returns Result.failure(Exception())

        val result = getAllProjectsUseCase.execute(invalidUserName)

        assertThat(result.exceptionOrNull()).isInstanceOf(NoUserFoundForProjectException::class.java)
        verify(exactly = 0) { projectRepository.getProjects() }
        verify(exactly = 1) { userRepository.getUserByUsername(invalidUserName) }
    }
}