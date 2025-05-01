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
import net.thechance.logic.use_cases.project.GetProjectsByUsernameUseCase
import net.thechance.logic.use_cases.project.exceptions.ProjectsLogicExceptions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class GetProjectsByUsernameUseCaseTest {
    
    private val projectRepository: ProjectsRepository = mockk(relaxed = true)
    private val userRepository: UserRepository = mockk(relaxed = true)

    private lateinit var fakeProjectVisibleForAllUsers: Project
    private lateinit var fakeProjectVisibleForAllUsersSecond: Project
    private lateinit var fakeProjectVisibleForAdminsOnly: Project
    private lateinit var adminUser: User
    private lateinit var secondAdminUser: User
    private lateinit var mateUser: User

    private lateinit var getProjectsByUsernameUseCase: GetProjectsByUsernameUseCase
    
    @BeforeEach
    fun setUp() {
        adminUser = User("1", "admin user", "abc123", UserType.AdminUser)
        mateUser = User("2", "mate user", "123", UserType.MateUser("1"))

        secondAdminUser = User("3", "second admin user", "1232#$", UserType.AdminUser)
        val firstState = State("1", "state1", "1")
        val secondState = State("2", "state2", "1")
        val firstTask = Task("1", "task1", "task des", firstState, "1")
        val secondTask = Task("2", "task2", "task des", secondState, "1")

        fakeProjectVisibleForAllUsers = createProject().copy(
            id = "1",
            states = mutableListOf(firstState, secondState),
            tasks = mutableListOf(firstTask, secondTask),
            createdBy = adminUser.name
        )

        fakeProjectVisibleForAllUsersSecond = createProject().copy(
            id = "2",
            createdBy = adminUser.name
        )

        fakeProjectVisibleForAdminsOnly = createProject().copy(
            states = mutableListOf(firstState, secondState),
            tasks = mutableListOf(firstTask, secondTask),
            createdBy = secondAdminUser.name
        )
        getProjectsByUsernameUseCase = GetProjectsByUsernameUseCase(projectRepository, userRepository)
    }

    @Test
    fun `should get all projects by project creator successfully, when user is admin`() {
        every { projectRepository.getProjects() } returns Result.success(
            listOf(fakeProjectVisibleForAllUsers, fakeProjectVisibleForAllUsersSecond, fakeProjectVisibleForAdminsOnly)
        )
        every { userRepository.getUserByUsername(secondAdminUser.name) } returns Result.success(secondAdminUser)

        val result = getProjectsByUsernameUseCase.execute(secondAdminUser.name, fakeProjectVisibleForAllUsers.createdBy)
        val expectedResult = listOf(fakeProjectVisibleForAllUsers, fakeProjectVisibleForAllUsersSecond)

        assertThat(result.getOrDefault(emptyList())).containsExactly(expectedResult)
        verify(exactly = 1) { projectRepository.getProjects() }
        verify(exactly = 1) { userRepository.getUserByUsername(adminUser.name) }
    }

    @Test
    fun `should get only relevant projects by project creator successfully, when user is mate`() {
        every { projectRepository.getProjects() } returns Result.success(
            listOf(fakeProjectVisibleForAllUsers, fakeProjectVisibleForAllUsersSecond, fakeProjectVisibleForAdminsOnly)
        )
        every { userRepository.getUserByUsername(mateUser.name) } returns Result.success(mateUser)

        val result = getProjectsByUsernameUseCase.execute(mateUser.name, fakeProjectVisibleForAllUsers.createdBy)
        val expectedResult = listOf(fakeProjectVisibleForAllUsers, fakeProjectVisibleForAllUsersSecond)

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

        val result = getProjectsByUsernameUseCase.execute(mateUser.name, fakeProjectVisibleForAdminsOnly.createdBy)

        assertThat(result.getOrNull()).isEmpty()
        verify(exactly = 0) { projectRepository.getProjects() }
        verify(exactly = 1) { userRepository.getUserByUsername(mateUser.name) }
    }

    @ParameterizedTest
    @CsvSource(
        "admin user, projectCreatorDoesNotExist",
        "userNameDoesNotExist, admin user",
    )
    fun `should throw exception, when username or project creator not found`(
        username: String,
        projectCreator: String,
    ) {
        every { userRepository.getUserByUsername(username) } returns Result.failure(Exception())

        val result = getProjectsByUsernameUseCase.execute(username, projectCreator)
        val expectedResult =
            if (fakeProjectVisibleForAllUsers.createdBy == projectCreator)
                NotAuthorizedUserException::class.java
            else NoUserFoundForProjectException::class.java

        assertThat(result.exceptionOrNull()).isInstanceOf(expectedResult)
        verify(exactly = 0) { projectRepository.getProjects() }
        verify(exactly = 1) { userRepository.getUserByUsername(username) }
    }

    @ParameterizedTest
    @CsvSource(
        "admin user, ''",
        "'', 1",
    )
    fun `should throw exception, when username or project creator is invalid `(
        username: String,
        projectCreator: String
    ) {
        every { userRepository.getUserByUsername(username) } returns Result.failure(Exception())

        val result = getProjectsByUsernameUseCase.execute(username, projectCreator)

        assertThat(result.exceptionOrNull()).isInstanceOf(InvalidUsernameForProjectException::class.java)
        verify(exactly = 0) { projectRepository.getProjects() }
        verify(exactly = 1) { userRepository.getUserByUsername(username) }
    }
}