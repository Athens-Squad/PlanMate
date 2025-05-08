package logic.use_cases.project

import com.google.common.truth.Truth.assertThat
import helper.project_helper.createProject
import helper.project_helper.fakes.FakeProjectData
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import logic.entities.Project
import logic.repositories.ProjectsRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class GetProjectByIdUseCaseTest {

    private val projectRepository: ProjectsRepository = mockk(relaxed = true)

    private lateinit var fakeProject: Project
    private lateinit var getProjectByIdUseCase: GetProjectByIdUseCase

    @BeforeEach
    fun setUp() {
        fakeProject = createProject().copy(
            id = "1",
            progressionStates = FakeProjectData.states,
            tasks = FakeProjectData.tasks
        )

        getProjectByIdUseCase = GetProjectByIdUseCase(projectRepository)
    }

    @Test
    fun `should get project by id successfully, when project is valid`() {
        runTest {
            val listOfProject = listOf(fakeProject)
            coEvery { projectRepository.getProjects() } returns listOfProject
            val result = getProjectByIdUseCase.execute(fakeProject.id)

            assertThat(listOfProject.contains(result)).isTrue()
            coVerify (exactly = 1) { projectRepository.getProjects() }
        }


    }

    @Test
    fun `should get project by id failed and throw exception,, when project not found to fetch`() {
        runTest {
            val notExistingProjectId = "projectIdDoNotExist"
            coEvery { projectRepository.getProjects() } returns listOf(fakeProject)

            assertThrows<Exception> { getProjectByIdUseCase.execute(notExistingProjectId)  }

            coVerify(exactly = 1) { projectRepository.getProjects() }
        }

    }

    @Test
    fun `should get project by id failed and throw exception, when project id is invalid`() {
        runTest {
            val invalidProjectId = ""
            coEvery { projectRepository.getProjects() } returns listOf(fakeProject)

            assertThrows<Exception> { getProjectByIdUseCase.execute(invalidProjectId) }
            coVerify (exactly = 0) { projectRepository.getProjects() }
        }
    }
}
