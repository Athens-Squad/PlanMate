package logic.use_cases.project

import data.projects.exceptions.ProjectsLogicExceptions
import data.projects.exceptions.ProjectsLogicExceptions.InvalidProjectNameException
import logic.entities.Project
import logic.repositories.ProjectsRepository
import logic.use_cases.project.projectValidations.checkIfFieldIsValid
import logic.use_cases.project.projectValidations.checkIfProjectExistInRepositoryAndReturn

class GetProjectByIdUseCase(
    private val projectRepository: ProjectsRepository,

) {
    suspend fun execute(projectId: String): Project {
        projectId.apply {
            checkIfFieldIsValid().takeIf { it } ?: throw InvalidProjectNameException()
        }

        return checkIfProjectExistInRepositoryAndReturn(projectId) { projectRepository.getProjects() }
            ?: throw ProjectsLogicExceptions.NoProjectFoundException()
    }
}