package logic.use_cases.project

import logic.entities.Project
import logic.repositories.ProjectsRepository
import data.projects.exceptions.ProjectsLogicExceptions
import data.projects.exceptions.ProjectsLogicExceptions.InvalidProjectNameException
import logic.repositories.StatesRepository
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