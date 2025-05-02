package logic.use_cases.project

import logic.entities.Project
import logic.repositories.ProjectsRepository
import logic.use_cases.project.exceptions.ProjectsLogicExceptions
import logic.use_cases.project.exceptions.ProjectsLogicExceptions.InvalidProjectNameException
import logic.use_cases.project.projectValidations.checkIfFieldIsValid
import logic.use_cases.project.projectValidations.checkIfProjectExistInRepositoryAndReturn

class GetProjectByIdUseCase(
    private val projectRepository: ProjectsRepository,
) {
    fun execute(projectId: String): Result<Project> {
        return runCatching {
            projectId.apply {
                checkIfFieldIsValid().takeIf { it } ?: throw InvalidProjectNameException()
            }

            checkIfProjectExistInRepositoryAndReturn(projectId) { projectRepository.getProjects() }
                ?: throw ProjectsLogicExceptions.NoProjectFoundException()
        }
    }
}