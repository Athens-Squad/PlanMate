@file:OptIn(ExperimentalUuidApi::class)

package net.thechance.logic.validators.projectValidations

import logic.entities.Project
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

interface ProjectValidator {
	fun validateProjectFieldsNotBlank(project: Project): Boolean
	suspend fun validateUserIsTheProjectOwner(projectId: Uuid, username: String): Boolean
	suspend fun validateUserIsAuthorized(username: String): Boolean
	suspend fun validateProjectNotExists(projectId: Uuid): Boolean
	suspend fun validateProjectAlreadyExists(projectId: Uuid): Boolean
}