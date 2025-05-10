@file:OptIn(ExperimentalUuidApi::class)

package logic.use_cases.project

import logic.entities.Project
import logic.repositories.ProjectsRepository
import logic.use_cases.project.projectValidations.checkIfFieldIsValid
import logic.use_cases.project.projectValidations.checkIfProjectExistInRepositoryAndReturn
import net.thechance.logic.exceptions.InvalidProjectNameException
import net.thechance.logic.exceptions.NoProjectFoundException
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class GetProjectByIdUseCase(
	private val projectRepository: ProjectsRepository,

	) {
	suspend fun execute(projectId: Uuid): Project {
		projectId.toString().apply {
			checkIfFieldIsValid().takeIf { it } ?: throw InvalidProjectNameException()
		}

		return checkIfProjectExistInRepositoryAndReturn(projectId) { projectRepository.getProjects() }
			?: throw NoProjectFoundException()
	}
}