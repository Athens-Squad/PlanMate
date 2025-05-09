package logic.use_cases.project

import com.google.common.truth.Truth.assertThat
import helper.project_helper.createProject
import helper.project_helper.fakes.FakeProjectData
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import logic.entities.Project
import logic.repositories.ProjectsRepository
import net.thechance.logic.exceptions.ProjectsLogicExceptions.InvalidProjectNameException
import net.thechance.logic.exceptions.ProjectsLogicExceptions.NoProjectFoundException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

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
        every { projectRepository.getProjects() } returns Result.success(listOf(fakeProject))

        val result = getProjectByIdUseCase.execute(fakeProject.id)

        assertThat(result.isSuccess).isTrue()
        verify(exactly = 1) { projectRepository.getProjects() }
    }

    @Test
    fun `should get project by id failed and throw exception,, when project not found to fetch`() {
        val notExistingProjectId = "projectIdDoNotExist"
        every { projectRepository.getProjects() } returns Result.success(listOf(fakeProject))

        val result = getProjectByIdUseCase.execute(notExistingProjectId)

        assertThat(result.exceptionOrNull()).isInstanceOf(NoProjectFoundException::class.java)
        verify(exactly = 1) { projectRepository.getProjects() }
    }

    @Test
    fun `should get project by id failed and throw exception, when project id is invalid`() {
        val invalidProjectId = ""
        every { projectRepository.getProjects() } returns Result.success(listOf(fakeProject))

        val result = getProjectByIdUseCase.execute(invalidProjectId)

        assertThat(result.exceptionOrNull()).isInstanceOf(InvalidProjectNameException::class.java)
        verify(exactly = 0) { projectRepository.getProjects() }
    }
}
