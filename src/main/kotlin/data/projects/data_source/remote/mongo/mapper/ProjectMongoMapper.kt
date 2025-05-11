@file:OptIn(ExperimentalUuidApi::class)

package data.projects.data_source.remote.mongo.mapper

import data.projects.data_source.remote.mongo.dto.ProjectDto
import logic.entities.Project
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