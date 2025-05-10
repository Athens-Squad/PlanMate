@file:OptIn(ExperimentalUuidApi::class)

package data.projects.data_source.localcsvfile.mapper

import logic.entities.Project
import data.projects.data_source.localcsvfile.dto.ProjectCsvDto
import kotlin.uuid.ExperimentalUuidApi

fun ProjectCsvDto.toProject() = Project(
	id = id,
	name = name,
	description = description,
	createdByUserName = createdByUserName
)

fun Project.toProjectCsvDto() = ProjectCsvDto(
	id = id,
	name = name,
	description = description,
	createdByUserName = createdByUserName
)