@file:OptIn(ExperimentalUuidApi::class, ExperimentalUuidApi::class, ExperimentalUuidApi::class)

package net.thechance.logic.validators.progressionStateValidations

import logic.entities.ProgressionState
import logic.exceptions.InvalidProgressionStateFieldsException
import logic.exceptions.NoProjectFoundForProgressionStateException
import logic.exceptions.ProgressionStateAlreadyExistsException
import logic.exceptions.ProgressionStateNotFoundException
import logic.repositories.ProgressionStateRepository
import logic.repositories.ProjectsRepository
import net.thechance.logic.validators.progressionStateValidations.ProgressionStateValidator
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid


class ProgressionStateValidatorImpl(
    private val projectsRepository: ProjectsRepository,
    private val progressionStateRepository: ProgressionStateRepository
) : ProgressionStateValidator {
	override fun validateProgressionStateFieldsNotBlank(progressionState: ProgressionState): Boolean {
		return when {
			progressionState.checkIsFieldsAreBlank() -> throw InvalidProgressionStateFieldsException()
			else -> { true }
		}
	}

	override suspend fun validateProjectExists(projectId: Uuid): Boolean {
		return when {
			checkIfProjectNotExists(projectId) -> throw NoProjectFoundForProgressionStateException()
			else -> { true }
		}
	}

	override suspend fun validateProgressionStateNotExists(progressionStateId: Uuid): Boolean {
		return when {
			getCurrentProgressionState(progressionStateId) != null -> throw ProgressionStateAlreadyExistsException()
			else -> { true }
		}
	}

	override suspend fun validateProgressionStateAlreadyExists(progressionStateId: Uuid): Boolean {
		return when {
			getCurrentProgressionState(progressionStateId) == null -> throw ProgressionStateNotFoundException()
			else -> { true }
		}
	}


	private fun ProgressionState.checkIsFieldsAreBlank(): Boolean {
		return id.toString().isBlank() || name.isBlank() || projectId.toString().isBlank()
	}

	private suspend fun getCurrentProgressionState(progressionStateId: Uuid): ProgressionState? {
		return progressionStateRepository.getProgressionStates().firstOrNull { it.id == progressionStateId }
	}

	private suspend fun checkIfProjectNotExists(projectId: Uuid): Boolean {
		return projectsRepository.getProjects().none { it.id == projectId }
	}
}