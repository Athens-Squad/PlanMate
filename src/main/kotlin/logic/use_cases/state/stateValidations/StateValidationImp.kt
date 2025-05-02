package logic.use_cases.state.stateValidations

import logic.entities.ProgressionState
import logic.repositories.ProjectsRepository
import logic.repositories.StatesRepository
import logic.use_cases.state.stateValidations.StateValidator


class StateValidationImp(
    private val projectsRepository: ProjectsRepository,
    private val statesRepository: StatesRepository
) : StateValidator {

    override fun validateProjectExists(projectId: String) {
        run {
            projectsRepository.getProjects().getOrNull()?.find { it.id == projectId }
                ?: throw IllegalArgumentException("Project with ID $projectId does not exist.")

        }
    }

    override fun stateIsExist(state: ProgressionState) {
        val stateExist = statesRepository.getStates().getOrNull()?.find { it.id == state.id } != null
        if (stateExist) {
            throw IllegalArgumentException("State with ID '${state.id}' already exists.")
        }
    }

    override fun validateStateFieldsIsNotBlankOrThrow(state: ProgressionState) {
        if (state.name.isBlank()) throw IllegalArgumentException("State name cannot be empty.")
    }
}