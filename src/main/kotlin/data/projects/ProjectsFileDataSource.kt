package net.thechance.data.projects

import logic.entities.Project
import net.thechance.data.csv_file_handle.CsvFileHandler
import net.thechance.data.csv_file_handle.CsvFileParser

class ProjectsFileDataSource(
    private val projectsFileHandler: CsvFileHandler,
    private val csvFileParser: CsvFileParser<Project>
) : ProjectDataSource {
    override fun saveProjectInCsvFile(project: Project): Result<Unit> {
        return runCatching {
        }
    }

    override fun updateProjectFromCsvFile(project: Project): Result<Unit> {
        return runCatching {  }
    }

    override fun deleteProjectFromCsvFile(projectId: String): Result<Unit> {
        return runCatching {  }
    }

    override fun getProjectsFromCsvFile(): Result<List<Project>> {
        return runCatching { emptyList() }
    }
}