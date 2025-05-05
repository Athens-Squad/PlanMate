package data.projects

import helper.project_helper.createProject
import io.mockk.mockk
import io.mockk.verify
import logic.entities.Project
import logic.repositories.ProjectsRepository
import net.thechance.data.projects.datasource.ProjectsDataSource
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ProjectsRepositoryImplTest {

    private val projectsDataSource: ProjectsDataSource = mockk(relaxed = true)
    private lateinit var projectsRepository: ProjectsRepository
    private val fakeProject: Project = createProject()

   @BeforeEach
    fun setUp() {
        projectsRepository = ProjectsRepositoryImpl(projectsDataSource)
    }

    @Test
    fun `should get all projects, when called`() {
        // When
        projectsRepository.getProjects()

        // Then
        verify(exactly = 1) { projectsDataSource.getProjects() }
    }

    @Test
    fun `should create project, when called`() {
        // When
        projectsRepository.createProject(fakeProject)

        // Then
        verify(exactly = 1) { projectsDataSource.createProject(fakeProject) }
    }

    @Test
    fun `should update project, when called`() {
        // Given
        val newFakeProject = createProject(
            name = "new Project",
            description = "new Data here",
            createdBy = "new user"
        )

        // When
        projectsRepository.updateProject(newFakeProject)

        // Then
        verify(exactly = 1) { projectsDataSource.updateProject(newFakeProject) }
    }

    @Test
    fun `should delete project, when called`() {
        // When
        projectsRepository.deleteProject(fakeProject.id)

        // Then
        verify(exactly = 1) { projectsDataSource.deleteProject(fakeProject.id) }
    }

}