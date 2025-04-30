package data.projects

import com.google.common.truth.Truth.assertThat
import helper.project_helper.createProject
import helper.project_helper.validRecordString
import helper.project_helper.validSecondRecordString
import io.mockk.mockk
import logic.entities.Project
import net.thechance.data.csv_file_handle.CsvFileHandler
import net.thechance.data.csv_file_handle.CsvFileParser
import net.thechance.data.projects.ProjectDataSource
import net.thechance.data.projects.ProjectsFileDataSource
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File

class ProjectsFileDataSourceTest {

    private val projectFileParser: CsvFileParser<Project> = mockk(relaxed = true)
    private lateinit var mockFile: File
    private lateinit var projectsFileHandler: CsvFileHandler
    private lateinit var fakeProject: Project
    private lateinit var projectsDataSource: ProjectDataSource

    @BeforeEach
    fun setUp() {
        fakeProject = createProject()

        mockFile = File.createTempFile("test", ".csv").apply { }
        projectsFileHandler = CsvFileHandler(mockFile)
        projectsDataSource = ProjectsFileDataSource(projectsFileHandler, projectFileParser)
    }

    @Test
    fun `should insert project in file, when saveProjectInCsvFile called`(){
        // When
        projectsDataSource.saveProjectInCsvFile(fakeProject)

        // Then
        assertThat(mockFile.readLines()).containsExactly(validRecordString)
    }

    @Test
    fun `should update project in file, when updateProjectFromCsvFile called`(){
        // Given
        val updatedProject = createProject(
            name = "Updated Project",
            description = "Just a simple updated project",
        )
        val updatedRecordString = "project1, Updated Project, Just a simple updated project, admin"

        // When
        projectsDataSource.saveProjectInCsvFile(fakeProject)
        projectsDataSource.updateProjectFromCsvFile(updatedProject)

        // Then
        assertThat(mockFile.readLines()).containsExactly(updatedRecordString)
    }

    @Test
    fun `should delete project in file, when deleteProjectFromCsvFile called`(){
        // When
        projectsDataSource.deleteProjectFromCsvFile(fakeProject.id)

        // Then
        assertThat(mockFile.readLines()).containsExactly(validSecondRecordString)
    }

    @Test
    fun `should get projects from file, when getProjectsFromCsvFile called`(){
        // When
        val projects = projectsDataSource.getProjectsFromCsvFile()

        // Then
        assertThat(projects.isSuccess).isTrue()
    }
}