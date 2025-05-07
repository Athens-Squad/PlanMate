package logic.use_cases.progression_state.progressionStateValidations

import logic.entities.ProgressionState
import logic.repositories.ProjectsRepository
import logic.repositories.ProgressionStateRepository
import net.thechance.data.progression_state.exceptions.DomainException
import net.thechance.data.progression_state.exceptions.InvalidProgressionStateFieldsException
import net.thechance.data.progression_state.exceptions.NoProjectFoundForProgressionStateException
import net.thechance.data.progression_state.exceptions.ProgressionStateAlreadyExistsException
import net.thechance.data.progression_state.exceptions.ProgressionStateException
import net.thechance.data.progression_state.exceptions.ProgressionStateNotFoundException
import net.thechance.logic.use_cases.progression_state.progressionStateValidations.ProgressionStateValidator


class ProgressionStateValidatorImpl(
    private val projectsRepository: ProjectsRepository,
    private val progressionStateRepository: ProgressionStateRepository
) : ProgressionStateValidator {

	override suspend fun validateBeforeCreation(progressionState: ProgressionState): ProgressionStateException? {
		return when {
			!progressionState.checkIsFieldsAreValid() -> InvalidProgressionStateFieldsException()
			!progressionState.checkIfProjectExists() -> NoProjectFoundForProgressionStateException()
			progressionState.checkIfProgressionStateExists() -> ProgressionStateAlreadyExistsException()
			else -> { null }
		}
	}

	override suspend fun validateAfterCreation(progressionStateId: String): DomainException? {
		val entity = progressionStateRepository.getProgressionStates().find { it.id == progressionStateId }
			?: return ProgressionStateNotFoundException()

		return when {
			!entity.checkIsFieldsAreValid() -> InvalidProgressionStateFieldsException()
			!entity.checkIfProjectExists() -> NoProjectFoundForProgressionStateException()
			else -> { null }
		}
	}

	private fun ProgressionState.checkIsFieldsAreValid(): Boolean {
		return id.isNotBlank() && name.isNotBlank() && projectId.isNotBlank()
	}

	private suspend fun ProgressionState.checkIfProgressionStateExists(): Boolean {
		return progressionStateRepository.getProgressionStates().any { it.id == id }
	}

	private suspend fun ProgressionState.checkIfProjectExists(): Boolean {
		return projectsRepository.getProjects().any { it.id == projectId }
	}
}