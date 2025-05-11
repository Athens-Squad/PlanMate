@file:OptIn(ExperimentalUuidApi::class)

package net.thechance.logic.use_cases.project.projectValidations

import logic.entities.Project
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

interface ProjectValidator {
	suspend fun validateProjectBeforeCreation(project: Project, username: String): Boolean
	suspend fun validateProjectAfterCreation(projectId: Uuid, username: String): Boolean
}