package net.thechance.logic.use_cases.project.projectValidations

import logic.entities.Project
import logic.entities.UserType
import logic.repositories.ProjectsRepository
import logic.repositories.UserRepository
import net.thechance.logic.exceptions.*

class ProjectValidatorImpl(
	private val userRepository: UserRepository,
	private val projectsRepository: ProjectsRepository
): ProjectValidator {
	override suspend fun validateProjectBeforeCreation(project: Project, username: String): Boolean {
		return when {
			username.checkIfUsernameIsNotValid() -> { throw InvalidUsernameForProjectException() }
			project.checkIfFieldIsNotValid() -> { throw InvalidProjectFieldsException() }
			!checkIfUserAuthorized(username) -> { throw NotAuthorizedUserException() }
			checkIfProjectExists(projectId = project.id)-> { throw ProjectAlreadyExistException() }
			else -> { true }
		}
	}

	override suspend fun validateProjectAfterCreation(projectId: String, username: String): Boolean {
		return when {
			username.checkIfUsernameIsNotValid() -> { throw InvalidUsernameForProjectException() }
			!checkIfUserAuthorized(username) -> { throw NotAuthorizedUserException() }
			projectId.isBlank() || !checkIfProjectExists(projectId) -> { throw NoProjectFoundException() }
			checkIfUserIsNotProjectOwner(username) -> { throw NotAuthorizedUserException() }
			else -> { true }
		}
	}

	private fun String.checkIfUsernameIsNotValid(): Boolean {
		return this.isBlank()
	}

	fun Project.checkIfFieldIsNotValid(): Boolean {
		return this.name.isBlank() && this.createdBy.isBlank()
	}

	private suspend fun checkIfUserAuthorized(username: String): Boolean {
		val user = userRepository.getUserByUsername(username)
		return when(user.type) {
			is UserType.AdminUser -> true
			is UserType.MateUser -> {
				checkIfUserAuthorized(user.type.adminName)
				false
			}
		}
	}

	private suspend fun checkIfUserIsNotProjectOwner(username: String): Boolean {
		return getProjects().none { it.createdBy == username }
	}

	private suspend fun checkIfProjectExists(projectId: String): Boolean {
		return getProjects().any { it.id == projectId }
	}

	private suspend fun getProjects(): List<Project> {
		return projectsRepository.getProjects()
	}
}