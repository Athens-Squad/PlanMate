package net.thechance.data.projects.datasource.localcsvfile

import data.csv_file_handle.CsvFileHandler
import data.csv_file_handle.CsvFileParser
import data.progression_state.data_source.ProgressionStateDataSource
import data.tasks.data_source.TasksDataSource
import logic.entities.Project
import net.thechance.data.projects.datasource.ProjectsDataSource

class ProjectsFileDataSource(
    private val projectsFileHandler: CsvFileHandler,
    private val csvFileParser: CsvFileParser<Project>,
    private val tasksFileDataSource: TasksDataSource,
    private val statesFileDataSource: ProgressionStateDataSource
) : ProjectsDataSource {

    override suspend fun createProject(project: Project) {
        val record = csvFileParser.toCsvRecord(project)
        projectsFileHandler.appendRecord(record)
    }

    override suspend fun updateProject(project: Project) {
        val updatedProjects = getProjects()
            .map { if (it.id == project.id) project else it }

        val updatedRecords = updatedProjects.map { csvFileParser.toCsvRecord(it) }
        projectsFileHandler.writeRecords(updatedRecords)
    }

    override suspend fun deleteProject(projectId: String) {
        val updatedProjects = getProjects()
            .filterNot { it.id == projectId}

        val updatedRecords = updatedProjects.map { csvFileParser.toCsvRecord(it) }
        projectsFileHandler.writeRecords(updatedRecords)
    }

    override suspend fun getProjects(): List<Project> {
        return projectsFileHandler.readRecords().map { record ->
            val project = csvFileParser.parseRecord(record)

            val tasks = tasksFileDataSource.getTasksByProjectId(project.id).getOrThrow().toMutableList()
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