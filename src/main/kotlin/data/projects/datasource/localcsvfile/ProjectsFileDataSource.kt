package net.thechance.data.projects.datasource.localcsvfile

import data.utils.csv_file_handle.CsvFileHandler
import data.utils.csv_file_handle.CsvFileParser
import data.progression_state.data_source.ProgressionStateDataSource
import data.tasks.data_source.TasksDataSource
import logic.entities.Project
import net.thechance.data.projects.datasource.ProjectsDataSource
import net.thechance.data.projects.dto.ProjectCsvDto
import net.thechance.data.projects.mappers.toProject
import net.thechance.data.projects.mappers.toProjectCsvDto

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

    override suspend fun deleteProject(projectId: String) {
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