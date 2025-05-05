package data.projects

import com.google.common.truth.Truth.assertThat
import data.csv_file_handle.CsvFileHandler
import data.csv_file_handle.CsvFileParser
import data.tasks.data_source.TasksDataSource
import helper.project_helper.createProject
import helper.project_helper.validRecordString
import io.mockk.every
import io.mockk.mockk
import logic.entities.Project
import net.thechance.data.projects.datasource.ProjectsDataSource
import net.thechance.data.projects.datasource.localcsvfile.ProjectsFileDataSource
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import java.io.File

class ProjectsFileDataSourceTest {

    private val projectFileParser: CsvFileParser<Project> = mockk(relaxed = true)
    private val tasksFileDataSource: TasksDataSource = mockk(relaxed = true)

    private lateinit var mockFile: File
    private lateinit var projectsFileHandler: CsvFileHandler

    private val fakeProject = createProject()
    private lateinit var projectsDataSource: ProjectsDataSource

    @BeforeEach
    fun setUp() {
        mockFile = File.createTempFile("projects", ".csv")
        projectsFileHandler = CsvFileHandler(mockFile)
        projectsDataSource = ProjectsFileDataSource(projectsFileHandler, projectFileParser, tasksFileDataSource)

        every { tasksFileDataSource.getTasksByProjectId(fakeProject.id) } returns Result.success(emptyList())
        every { projectFileParser.toCsvRecord(fakeProject) } returns validRecordString
        every { projectFileParser.parseRecord(validRecordString) } returns fakeProject
    }

    @AfterEach
    fun tearDown() {
       mockFile.delete()
    }

    @Test
    fun `should insert project in file, when saveProjectInCsvFile called`(){
        // When
        projectsDataSource.createProject(fakeProject)

        // Then
        assertThat(mockFile.readLines()).containsExactly(validRecordString)
    }

    @ParameterizedTest
    @CsvSource(
        "project1, Updated Project",
        "project2, Plan Mate"
    )
    fun `should update project in file, when valid new Project is passed`(
        projectId: String,
        expectedProjectName: String
    ){
        // Given
        val updatedProject = createProject(
            id = projectId,
            name = "Updated Project",
            description = "Just a simple updated project",
        )
        val updatedRecordString = "project1, Updated Project, Just a simple updated project, admin"

        every { projectFileParser.toCsvRecord(updatedProject) } returns updatedRecordString

        // When
        projectsDataSource.createProject(fakeProject)
        projectsDataSource.updateProject(updatedProject)
        val projectNameInFile = mockFile.readLines().first().split(",")[1].trim()

        // Then
        assertThat(projectNameInFile).isEqualTo(expectedProjectName)
    }

    @Test
    fun `should not update project in file, when invalid new Project is passed`(){
        // Given
        val updatedProject = createProject(
            name = "Updated Project",
            description = "Just a simple updated project",
        )
        val updatedRecordString = "project1, Updated Project, Just a simple updated project, admin"

        every { projectFileParser.toCsvRecord(updatedProject) } returns updatedRecordString

        // When
        projectsDataSource.createProject(fakeProject)
        projectsDataSource.updateProject(updatedProject)

        // Then
        assertThat(mockFile.readLines()).contains(updatedRecordString)
    }

    @Test
    fun `should delete project in file, when deleteProjectFromCsvFile called`(){
       // When
        projectsDataSource.createProject(fakeProject)
        projectsDataSource.deleteProject(fakeProject.id)

        // Then
        assertThat(mockFile.readLines()).isEmpty()
    }

    @Test
    fun `should get projects from file, when getProjectsFromCsvFile called`(){
        // Given
        projectsDataSource.createProject(fakeProject)

        // When
        val projects = projectsDataSource.getProjects()

        // Then
        assertThat(projects.getOrDefault(emptyList())).isNotEmpty()
    }
}