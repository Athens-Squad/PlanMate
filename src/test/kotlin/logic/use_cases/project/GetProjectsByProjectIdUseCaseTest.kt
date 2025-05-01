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
import net.thechance.logic.use_cases.project.GetProjectByProjectIdUseCase
import net.thechance.logic.use_cases.project.exceptions.ProjectsLogicExceptions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class GetProjectsByProjectIdUseCaseTest {
     private val projectRepository: ProjectsRepository = mockk(relaxed = true)
     private val userRepository: UserRepository = mockk(relaxed = true)

     private lateinit var fakeProjectVisibleForAllUsers: Project
     private lateinit var fakeProjectVisibleForAdminsOnly: Project
     private lateinit var adminUser: User
     private lateinit var secondAdminUser: User
     private lateinit var mateUser: User

     private lateinit var getAllProjectsUseCaseById: GetProjectByProjectIdUseCase

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
            id = "1",
            states = mutableListOf(firstState, secondState),
            tasks = mutableListOf(firstTask, secondTask),
            createdBy = adminUser.name
         )

         fakeProjectVisibleForAdminsOnly = createProject().copy(
             id = "2",
             states = mutableListOf(firstState, secondState),
             tasks = mutableListOf(firstTask, secondTask),
             createdBy = secondAdminUser.name
         )
         getAllProjectsUseCaseById = GetProjectByProjectIdUseCase(projectRepository, userRepository)
     }

    @Test
    fun `should get project by project ID successfully, when user is admin`() {
        every { projectRepository.getProjects() } returns Result.success(
            listOf(fakeProjectVisibleForAllUsers, fakeProjectVisibleForAdminsOnly)
        )
        every { userRepository.getUserByUsername(adminUser.name) } returns Result.success(adminUser)

        val result = getAllProjectsUseCaseById.execute(adminUser.name, fakeProjectVisibleForAllUsers.id)

        assertThat(result).isEqualTo(fakeProjectVisibleForAllUsers)
        verify(exactly = 1) { projectRepository.getProjects() }
        verify(exactly = 1) { userRepository.getUserByUsername(adminUser.name) }
    }

    @Test
    fun `should get only relevant project by project ID successfully, when user is mate`() {
        every { projectRepository.getProjects() } returns Result.success(
            listOf(fakeProjectVisibleForAllUsers, fakeProjectVisibleForAdminsOnly)
        )
        every { userRepository.getUserByUsername(mateUser.name) } returns Result.success(mateUser)

        val result = getAllProjectsUseCaseById.execute(mateUser.name, fakeProjectVisibleForAllUsers.id)

        assertThat(result).isEqualTo(fakeProjectVisibleForAllUsers)
        verify(exactly = 1) { projectRepository.getProjects() }
        verify(exactly = 1) { userRepository.getUserByUsername(mateUser.name) }
    }

    @Test
    fun `should throw exception, when no relevant project exist for mate user`() {
        every { projectRepository.getProjects() } returns Result.success(
            listOf(fakeProjectVisibleForAllUsers, fakeProjectVisibleForAdminsOnly)
        )
        every { userRepository.getUserByUsername(mateUser.name) } returns Result.success(mateUser)

        val result = getAllProjectsUseCaseById.execute(mateUser.name, fakeProjectVisibleForAdminsOnly.id)

        assertThat(result.exceptionOrNull()).isInstanceOf(NotAuthorizedUserException::class.java)
        verify(exactly = 0) { projectRepository.getProjects() }
        verify(exactly = 1) { userRepository.getUserByUsername(mateUser.name) }
    }

    @ParameterizedTest
    @CsvSource(
        "admin user, projectIDDoesNotExist",
        "userNameDoesNotExist, 1",
    )
    fun `should throw exception, when username or project ID not found`(
        username: String,
        projectID: String,
    ) {
        every { userRepository.getUserByUsername(username) } returns Result.failure(Exception())

        val result = getAllProjectsUseCaseById.execute(username, projectID)
        val expectedResult =
            if (fakeProjectVisibleForAllUsers.id == projectID)
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
    fun `should throw exception, when username or project ID is invalid `(
        username: String,
        projectID: String
    ) {
        every { userRepository.getUserByUsername(username) } returns Result.failure(Exception())

        val result = getAllProjectsUseCaseById.execute(username, projectID)

        assertThat(result.exceptionOrNull()).isInstanceOf(InvalidUsernameForProjectException::class.java)
        verify(exactly = 0) { projectRepository.getProjects() }
        verify(exactly = 1) { userRepository.getUserByUsername(username) }
    }
 }
