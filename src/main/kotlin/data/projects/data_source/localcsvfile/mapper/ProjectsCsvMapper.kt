package data.projects.data_source.localcsvfile.mapper

import logic.entities.Project
import data.projects.data_source.localcsvfile.dto.ProjectCsvDto

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