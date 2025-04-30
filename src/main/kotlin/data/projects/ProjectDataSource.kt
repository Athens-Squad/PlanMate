package net.thechance.data.projects

import logic.entities.Project

interface ProjectDataSource {
    fun saveProjectInCsvFile(project: Project): Result<Unit>
    fun updateProjectFromCsvFile(project: Project): Result<Unit>
    fun deleteProjectFromCsvFile(projectId: String): Result<Unit>
    fun getProjectsFromCsvFile(): Result<List<Project>>
}