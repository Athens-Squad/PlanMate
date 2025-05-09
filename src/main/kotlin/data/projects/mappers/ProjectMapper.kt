package net.thechance.data.projects.mappers

import logic.entities.Project
import net.thechance.data.projects.dto.ProjectCsvDto
import net.thechance.data.projects.dto.ProjectDto

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

fun ProjectCsvDto.toProject() = Project(
	id = id,
	name = name,
	description = description,
	createdBy = createdBy
)

fun Project.toProjectCsvDto() = ProjectCsvDto(
	id = id,
	name = name,
	description = description,
	createdBy = createdBy
)