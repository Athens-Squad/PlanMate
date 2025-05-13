@file:OptIn(ExperimentalUuidApi::class, ExperimentalUuidApi::class, ExperimentalUuidApi::class)

package net.thechance.logic.validators.projectValidations

import logic.entities.Project
import logic.entities.UserType
import logic.repositories.ProjectsRepository
import logic.repositories.UserRepository
import net.thechance.logic.exceptions.*
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class ProjectValidatorImpl(
	private val userRepository: UserRepository,
	private val projectsRepository: ProjectsRepository
): ProjectValidator {
	override fun validateProjectFieldsNotBlank(project: Project): Boolean {
		return when {
			project.checkIfFieldsAreBlank() -> throw InvalidProjectFieldsException()
			else -> { true }
		}
	}

	override suspend fun validateUserIsAuthorized(username: String): Boolean {
		return when {
			username.isBlank() -> throw InvalidUsernameForProjectException()
			checkIfUserNotAuthorized(username) -> throw NotAuthorizedUserException()
			else -> { true }
		}
	}

	override suspend fun validateUserIsTheProjectOwner(projectId: Uuid, username: String): Boolean {
		return when {
			checkIfUserIsNotTheProjectOwner(projectId, username) -> throw NotAuthorizedUserException()
			else -> { true }
		}
	}

	override suspend fun validateProjectNotExists(projectId: Uuid): Boolean {
		return when {
			getCurrentProject(projectId) != null -> throw ProjectAlreadyExistException()
			else -> { true }
		}
	}

	override suspend fun validateProjectAlreadyExists(projectId: Uuid): Boolean {
		return when {
			getCurrentProject(projectId) == null -> throw NoProjectFoundException()
			else -> { true }
		}
	}

	private fun Project.checkIfFieldsAreBlank(): Boolean {
		return name.isBlank() || createdByUserName.isBlank()
	}

	private suspend fun checkIfUserNotAuthorized(username: String): Boolean {
		val user = userRepository.getUserByUsername(username)
		return when(user.type) {
			is UserType.AdminUser -> false
			is UserType.MateUser -> {
				checkIfUserNotAuthorized(user.type.adminName)
				true
			}
		}
	}

	private suspend fun checkIfUserIsNotTheProjectOwner(projectId: Uuid, username: String): Boolean {
		return getCurrentProject(projectId) !in getAllUserProjects(username)
	}

	private suspend fun getAllUserProjects(username: String): List<Project> {
		return getProjects().filter { it.createdByUserName == username }
	}

	private suspend fun getCurrentProject(projectId: Uuid): Project? {
		return getProjects().firstOrNull { it.id == projectId }
	}

	private suspend fun getProjects(): List<Project> {
		return projectsRepository.getProjects()
	}
}