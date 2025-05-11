package net.thechance.logic.use_cases.project.projectValidations

import logic.entities.Project

interface ProjectValidator {
	suspend fun validateProjectBeforeCreation(project: Project, username: String): Boolean
	suspend fun validateProjectAfterCreation(projectId: String, username: String): Boolean
}