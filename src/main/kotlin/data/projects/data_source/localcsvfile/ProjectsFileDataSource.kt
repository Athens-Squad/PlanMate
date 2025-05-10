package data.projects.data_source.localcsvfile

import data.progression_state.data_source.ProgressionStateDataSource
import data.tasks.data_source.TasksDataSource
import data.utils.csv_file_handle.CsvFileHandler
import data.utils.csv_file_handle.CsvFileParser
import logic.entities.Project
import data.projects.data_source.ProjectsDataSource
import data.projects.data_source.localcsvfile.dto.ProjectCsvDto
import data.projects.data_source.localcsvfile.mapper.toProject
import data.projects.data_source.localcsvfile.mapper.toProjectCsvDto
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class ProjectsFileDataSource(
	private val projectsFileHandler: CsvFileHandler,
	private val csvFileParser: CsvFileParser<ProjectCsvDto>,
	private val tasksFileDataSource: TasksDataSource,
	private val statesFileDataSource: ProgressionStateDataSource
) : ProjectsDataSource {

    override suspend fun createProject(project: Project) {
        val record = csvFileParser.toCsvRecord(project.toProjectCsvDto())
        projectsFileHandler.appendRecord(record)
    }

    override suspend fun updateProject(project: Project) {
        val updatedProjects = getProjects()
            .map { if (it.id == project.id) project else it }

        val updatedRecords = updatedProjects.map { csvFileParser.toCsvRecord(it.toProjectCsvDto()) }
        projectsFileHandler.writeRecords(updatedRecords)
    }

    override suspend fun deleteProject(projectId: Uuid) {
        val updatedProjects = getProjects()
            .filterNot { it.id == projectId}

        val updatedRecords = updatedProjects.map { csvFileParser.toCsvRecord(it.toProjectCsvDto()) }
        projectsFileHandler.writeRecords(updatedRecords)
    }

    override suspend fun getProjects(): List<Project> {
        return projectsFileHandler.readRecords().map { record ->
            val project = csvFileParser.parseRecord(record).toProject()

            val tasks = tasksFileDataSource.getTasksByProjectId(project.id).toMutableList()
            val states = statesFileDataSource.getProgressionStates()
                .filter { it.projectId == project.id }
                .toMutableList()

            project.copy(
                tasks = tasks,
                progressionStates = states
            )
        }
    }
}