package data.projects.data_source.remote.mongo.mapper

import logic.entities.Project
import data.projects.data_source.remote.mongo.dto.ProjectDto

fun ProjectDto.toProject() = Project(
	id = id,
	name = name,
	description = description,
	createdBy = createdBy
)

fun Project.toProjectDto() = ProjectDto(
	id = id,
	name = name,
	description = description,
	createdBy = createdBy
)