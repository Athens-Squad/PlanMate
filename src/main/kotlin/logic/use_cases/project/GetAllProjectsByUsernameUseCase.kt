package logic.use_cases.project

import logic.entities.Project
import logic.repositories.ProjectsRepository
import logic.repositories.UserRepository
import logic.use_cases.project.projectValidations.checkIfFieldIsValid
import logic.use_cases.project.projectValidations.checkIfUserAuthorized
import logic.use_cases.project.projectValidations.checkIfUserIsProjectOwner
import net.thechance.logic.exceptions.InvalidUsernameForProjectException
import net.thechance.logic.exceptions.NoProjectFoundException
import net.thechance.logic.exceptions.NotAuthorizedUserException

class GetAllProjectsByUsernameUseCase(
    private val projectRepository: ProjectsRepository,
    private val userRepository: UserRepository
) {
    suspend fun execute(username: String): List<Project> {
        username.apply {
            checkIfFieldIsValid().takeIf { it } ?: throw InvalidUsernameForProjectException()

            checkIfUserAuthorized(username) { userRepository.getUserByUsername(it) }
                .takeIf { it } ?: throw NotAuthorizedUserException()
        }

        return projectRepository.getProjects()
            .filter {  project ->
                checkIfUserIsProjectOwner(username, project.createdBy)
            }
            .ifEmpty { throw NoProjectFoundException() }
    }
}