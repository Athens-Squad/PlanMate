@file:OptIn(ExperimentalUuidApi::class)

package net.thechance.logic.use_cases.progression_state.progressionStateValidations

import logic.entities.ProgressionState
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

interface ProgressionStateValidator {
	suspend fun validateBeforeCreation(progressionState: ProgressionState): Boolean
	suspend fun validateAfterCreation(progressionStateId: Uuid): Boolean
}