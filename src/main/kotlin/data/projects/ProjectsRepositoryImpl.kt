@file:OptIn(ExperimentalUuidApi::class)

package data.projects

import data.projects.data_source.ProjectsDataSource
import logic.entities.Project
import logic.repositories.ProjectsRepository
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class ProjectsRepositoryImpl(
    private val projectsDataSource: ProjectsDataSource
) : ProjectsRepository, ProjectsDataSource by projectsDataSource