package net.thechance.logic.use_cases.progression_state.progressionStateValidations

import logic.entities.ProgressionState
import net.thechance.data.progression_state.exceptions.DomainException

interface ProgressionStateValidator {
	suspend fun validateBeforeCreation(progressionState: ProgressionState): DomainException?
	suspend fun validateAfterCreation(progressionStateId: String): DomainException?
}