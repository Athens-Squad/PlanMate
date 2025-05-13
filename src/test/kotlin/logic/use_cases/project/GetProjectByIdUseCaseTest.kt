package logic.use_cases.project

import helper.project_helper.createProject
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import logic.entities.Project
import logic.repositories.ProjectsRepository
import net.thechance.logic.exceptions.NoProjectFoundException
import net.thechance.logic.use_cases.project.projectValidations.ProjectValidator
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.BeforeTest
import kotlin.uuid.ExperimentalUuidApi

class GetProjectByIdUseCaseTest {
    private lateinit var getProjectByIdUseCase: GetProjectByIdUseCase
    private lateinit var projectsRepository: ProjectsRepository
    private lateinit var projectValidator: ProjectValidator

    @BeforeTest
    fun setup() {
        projectsRepository = mockk(relaxed = true)
        projectValidator = mockk(relaxed = true)
        getProjectByIdUseCase = GetProjectByIdUseCase(projectsRepository, projectValidator)
    }

    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun `should return project if it exists`() {
        runTest {
            //given
            val dummyProject = createProject()
            coEvery { projectValidator.validateProjectAlreadyExists(dummyProject.id) } returns true
            coEvery { projectsRepository.getProjects() } returns listOf(dummyProject)

            //when
            val result = getProjectByIdUseCase.execute(dummyProject.id)
            //then
            assertEquals(dummyProject, result)
            coVerify { projectsRepository.getProjects() }

        }

    }

    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun `should not return project if it not exist`() {
        runTest {
            //given
            val dummyProject = createProject()

            coEvery { projectValidator.validateProjectAlreadyExists(dummyProject.id) } throws NoProjectFoundException()
            //when & then
            assertThrows<NoProjectFoundException> {
                getProjectByIdUseCase.execute(dummyProject.id)
            }

        }
    }

}
