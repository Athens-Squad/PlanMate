package net.thechance.logic.use_cases.progression_state.progressionStateValidations

import logic.entities.ProgressionState
import logic.exceptions.ProgressionStateException

interface ProgressionStateValidator {
	suspend fun validateBeforeCreation(progressionState: ProgressionState): ProgressionStateException?
	suspend fun validateAfterCreation(progressionStateId: String): ProgressionStateException?
}