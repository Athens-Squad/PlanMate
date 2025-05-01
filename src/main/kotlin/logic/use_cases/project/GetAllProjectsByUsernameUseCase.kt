package net.thechance.logic.use_cases.project

import logic.entities.Project
import logic.repositories.ProjectsRepository
import logic.repositories.UserRepository
import net.thechance.logic.use_cases.project.exceptions.ProjectsLogicExceptions.*
import net.thechance.logic.use_cases.project.log_builder.createLog
import net.thechance.logic.use_cases.project.validations.checkIfFieldIsValid
import net.thechance.logic.use_cases.project.validations.checkIfProjectExistInRepositoryAndReturn
import net.thechance.logic.use_cases.project.validations.checkIfUserAuthorized
import net.thechance.logic.use_cases.project.validations.checkIfUserIsProjectOwner

class GetAllProjectsByUsernameUseCase(
    private val projectRepository: ProjectsRepository,
    private val userRepository: UserRepository
) {
    fun execute(username: String): Result<List<Project>> {
        return runCatching {
            username.apply {
                checkIfFieldIsValid().takeIf { it } ?: throw InvalidUsernameForProjectException()
                checkIfUserAuthorized(username) { userRepository.getUserByUsername(it) }
                    .takeIf { it } ?: throw NotAuthorizedUserException()
            }

            projectRepository.getProjects()
                .fold(
                    onSuccess = { projects -> projects.filter {  project ->
                            checkIfUserIsProjectOwner(username, project.createdBy)
                        }
                    },
                    onFailure = { throw NoProjectFoundException() }
                )
        }
    }
}