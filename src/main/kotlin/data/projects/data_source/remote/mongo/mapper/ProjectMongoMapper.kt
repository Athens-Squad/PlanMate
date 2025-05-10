@file:OptIn(ExperimentalUuidApi::class)

package data.projects.data_source.remote.mongo.mapper

import logic.entities.Project
import data.projects.data_source.remote.mongo.dto.ProjectDto
import kotlin.uuid.ExperimentalUuidApi

fun ProjectDto.toProject() = Project(
	id = id,
	name = name,
	description = description,
	createdByUserName = createdByUserName
)

fun Project.toProjectDto() = ProjectDto(
	id = id,
	name = name,
	description = description,
	createdByUserName = createdByUserName
)