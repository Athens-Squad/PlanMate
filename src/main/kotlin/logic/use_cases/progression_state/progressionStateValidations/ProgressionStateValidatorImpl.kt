@file:OptIn(ExperimentalUuidApi::class)

package logic.use_cases.progression_state.progressionStateValidations

import logic.entities.ProgressionState
import logic.repositories.ProjectsRepository
import logic.repositories.ProgressionStateRepository
import logic.exceptions.InvalidProgressionStateFieldsException
import logic.exceptions.NoProjectFoundForProgressionStateException
import logic.exceptions.ProgressionStateAlreadyExistsException
import logic.exceptions.ProgressionStateNotFoundException
import net.thechance.logic.use_cases.progression_state.progressionStateValidations.ProgressionStateValidator
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid


class ProgressionStateValidatorImpl(
    private val projectsRepository: ProjectsRepository,
    private val progressionStateRepository: ProgressionStateRepository
) : ProgressionStateValidator {

	override suspend fun validateBeforeCreation(progressionState: ProgressionState): Boolean {
		return when {
			progressionState.checkIsFieldsAreBlank() -> throw InvalidProgressionStateFieldsException()
			!progressionState.checkIfProjectExists() -> throw NoProjectFoundForProgressionStateException()
			progressionState.checkIfProgressionStateExists() -> throw ProgressionStateAlreadyExistsException()
			else -> { true }
		}
	}

	override suspend fun validateAfterCreation(progressionStateId: Uuid): Boolean {
		val entity = progressionStateRepository.getProgressionStates().find { it.id == progressionStateId }
			?: throw ProgressionStateNotFoundException()

		return when {
			entity.checkIsFieldsAreBlank() -> throw InvalidProgressionStateFieldsException()
			!entity.checkIfProjectExists() -> throw NoProjectFoundForProgressionStateException()
			else -> { true }
		}
	}

	private fun ProgressionState.checkIsFieldsAreBlank(): Boolean {
		return id.toString().isNotBlank() || name.isNotBlank() || projectId.toString().isNotBlank()
	}

	private suspend fun ProgressionState.checkIfProgressionStateExists(): Boolean {
		return progressionStateRepository.getProgressionStates().any { it.id == id }
	}

	private suspend fun ProgressionState.checkIfProjectExists(): Boolean {
		return projectsRepository.getProjects().any { it.id == projectId }
	}
}