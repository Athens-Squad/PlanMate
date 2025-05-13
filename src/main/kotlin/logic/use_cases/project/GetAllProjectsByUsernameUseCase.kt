@file:OptIn(ExperimentalUuidApi::class)

package logic.use_cases.project

import logic.entities.Project
import logic.repositories.ProjectsRepository
import net.thechance.logic.validators.projectValidations.ProjectValidator
import kotlin.uuid.ExperimentalUuidApi

class GetAllProjectsByUsernameUseCase(
    private val projectRepository: ProjectsRepository,
    private val projectValidator: ProjectValidator,
) {
    suspend fun execute(username: String): List<Project> {
		projectValidator.validateUserIsAuthorized(username)

        return projectRepository.getProjects().filter { it.createdByUserName == username }
    }
}