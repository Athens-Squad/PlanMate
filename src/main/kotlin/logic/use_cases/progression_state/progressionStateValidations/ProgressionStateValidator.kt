package net.thechance.logic.use_cases.progression_state.progressionStateValidations

import logic.entities.ProgressionState

interface ProgressionStateValidator {
	suspend fun validateBeforeCreation(progressionState: ProgressionState): Boolean
	suspend fun validateAfterCreation(progressionStateId: String): Boolean
}