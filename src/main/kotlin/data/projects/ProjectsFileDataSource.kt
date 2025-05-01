package data.projects

import data.csv_file_handle.CsvFileHandler
import data.csv_file_handle.CsvFileParser
import data.tasks.data_source.TasksDataSource
import logic.entities.Project

class ProjectsFileDataSource(
    private val projectsFileHandler: CsvFileHandler,
    private val csvFileParser: CsvFileParser<Project>,
    private val tasksFileDataSource: TasksDataSource,
) : ProjectsDataSource {

    override fun saveProjectInCsvFile(project: Project): Result<Unit> {
        return runCatching {
            val record = csvFileParser.toCsvRecord(project)
            projectsFileHandler.appendRecord(record)
        }
    }

    override fun updateProjectFromCsvFile(project: Project): Result<Unit> {
        return runCatching {
            val updatedProjects = getProjectsFromCsvFile()
                .getOrThrow()
                .map { if (it.id == project.id) project else it }

            val updatedRecords = updatedProjects.map { csvFileParser.toCsvRecord(it) }
            projectsFileHandler.writeRecords(updatedRecords)
        }
    }

    override fun deleteProjectFromCsvFile(projectId: String): Result<Unit> {
        return runCatching {
            val updatedProjects = getProjectsFromCsvFile()
                .getOrThrow()
                .filterNot { it.id == projectId}

            val updatedRecords = updatedProjects.map { csvFileParser.toCsvRecord(it) }
            projectsFileHandler.writeRecords(updatedRecords)
        }
    }

    override fun getProjectsFromCsvFile(): Result<List<Project>> {
        return runCatching {
            projectsFileHandler.readRecords().map { record ->
                csvFileParser.parseRecord(record).also { project ->
                    val tasks = tasksFileDataSource.getTasksByProjectId(project.id).getOrThrow().toMutableList()
                    project.copy(tasks = tasks)
                }
            }
        }
    }
}