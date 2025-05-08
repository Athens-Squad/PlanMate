package logic.use_cases.project

import helper.project_helper.createProject
import helper.project_helper.fakes.FakeProjectData
import helper.project_helper.fakes.FakeProjectData.adminUser
import helper.project_helper.fakes.FakeProjectData.mateUserForAdminUser
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import logic.entities.Project
import logic.repositories.AuditRepository
import logic.repositories.ProjectsRepository
import logic.repositories.UserRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class UpdateProjectUseCaseTest {

    private val projectRepository: ProjectsRepository = mockk(relaxed = true)
    private val userRepository: UserRepository = mockk(relaxed = true)

    private val auditRepository: AuditRepository = mockk(relaxed = true)
    private lateinit var fakeProject: Project
    private lateinit var updatedProject: Project
    private lateinit var updateProjectUseCase: UpdateProjectUseCase

    @BeforeEach
    fun setUp() {
        fakeProject = createProject().copy(
            id = "1",
            progressionStates = FakeProjectData.states,
            tasks = FakeProjectData.tasks,
        )
        updatedProject = fakeProject.copy(
            name = "Updated Project",
            description = "Just a simple updated project",
        )

        updateProjectUseCase =
            UpdateProjectUseCase(projectRepository, userRepository, auditRepository)
    }

    @Test
    fun `should update project successfully, when update is valid`() {
        runTest {
            coEvery { userRepository.getUserByUsername(updatedProject.createdBy) } returns adminUser
            coEvery { projectRepository.getProjects() } returns listOf(fakeProject)
            coEvery { projectRepository.updateProject(updatedProject) } returns Unit
            coEvery { auditRepository.createAuditLog(any()) } returns Unit

            updateProjectUseCase.execute(updatedProject)

            coVerify(exactly = 1) { projectRepository.updateProject(updatedProject) }
            coVerify(exactly = 1) { projectRepository.getProjects() }
            coVerify(exactly = 1) { userRepository.getUserByUsername(updatedProject.createdBy) }
            coVerify(exactly = 1) { auditRepository.createAuditLog(any()) }
        }

    }

    @Test
    fun `should update project failed and throw exception, when user is not admin`() {
        runTest {
            coEvery { userRepository.getUserByUsername(updatedProject.createdBy) } returns mateUserForAdminUser

            assertThrows<Exception> {updateProjectUseCase.execute(updatedProject)  }

            coVerify(exactly = 0) { projectRepository.updateProject(updatedProject) }
            coVerify(exactly = 1) { userRepository.getUserByUsername(updatedProject.createdBy) }
        }

    }

    @Test
    fun `should update project failed and throw exception, when project is not found for updated project`() {
        runTest {
            val newUpdatedProject = updatedProject.copy(id = "project2")
            coEvery { userRepository.getUserByUsername(newUpdatedProject.createdBy) } returns adminUser
            coEvery { projectRepository.getProjects() } returns listOf(fakeProject, updatedProject)

            assertThrows<Exception> { updateProjectUseCase.execute(newUpdatedProject) }

            coVerify (exactly = 0) { projectRepository.updateProject(newUpdatedProject) }
            coVerify(exactly = 1) { projectRepository.getProjects() }
            coVerify(exactly = 1) { userRepository.getUserByUsername(newUpdatedProject.createdBy) }
        }

    }


    @Test
    fun `should update project failed and throw exception, when updated project user name is invalid`() {
        runTest {
            val updatedProjectWithInvalidUserName = updatedProject.copy(createdBy = "")
            coEvery { userRepository.getUserByUsername(updatedProjectWithInvalidUserName.createdBy) } throws Exception()

            assertThrows<Exception> { updateProjectUseCase.execute(updatedProjectWithInvalidUserName) }
            coVerify(exactly = 0) { projectRepository.updateProject(updatedProjectWithInvalidUserName) }
            coVerify(exactly = 0) { userRepository.getUserByUsername(updatedProjectWithInvalidUserName.createdBy) }
        }

    }

    @Test
    fun `should update project failed and throw exception, when updated project name is invalid`() {
        runTest {
            val updatedProjectWithInvalidName = updatedProject.copy(name = "")
            coEvery { userRepository.getUserByUsername(updatedProjectWithInvalidName.createdBy) } returns adminUser


            assertThrows<Exception> { updateProjectUseCase.execute(updatedProjectWithInvalidName) }
            coVerify(exactly = 0) { projectRepository.updateProject(updatedProjectWithInvalidName) }
        }

    }
}