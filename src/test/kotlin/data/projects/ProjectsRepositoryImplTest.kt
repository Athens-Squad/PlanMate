package data.projects

import com.google.common.truth.Truth.assertThat
import helper.project_helper.createProject
import logic.entities.Project
import logic.repositories.ProjectsRepository
import net.thechance.data.projects.ProjectsRepositoryImpl
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ProjectsRepositoryImplTest {

    private lateinit var projectsRepository: ProjectsRepository
    private lateinit var fakeProject: Project

   @BeforeEach
    fun setUp() {
        projectsRepository = ProjectsRepositoryImpl()
        fakeProject = createProject()
    }

    @Test
    fun `should get all projects, when called`() {
        // When
        val projects = projectsRepository.getProjects()

        // Then
        assertThat(projects.isSuccess).isTrue()
    }

    @Test
    fun `should create project, when called`() {
        // When
        projectsRepository.createProject(fakeProject)
        val projects = projectsRepository.getProjects()

        // Then
        assertThat(projects.getOrNull()).containsExactly(fakeProject)
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
        val projects = projectsRepository.getProjects()

        // Then
        assertThat(projects.getOrNull()).containsExactly(newFakeProject)
    }

    @Test
    fun `should delete project, when called`() {
        // When
        val projects = projectsRepository.getProjects()
        projectsRepository.deleteProject(fakeProject.id)

        // Then
        assertThat(projects.getOrNull()).isEmpty()
    }

}