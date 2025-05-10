@file:OptIn(ExperimentalUuidApi::class)

package net.thechance.logic.use_cases.progression_state.progressionStateValidations

import logic.entities.ProgressionState
import logic.exceptions.ProgressionStateException
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

interface ProgressionStateValidator {
	suspend fun validateBeforeCreation(progressionState: ProgressionState): ProgressionStateException?
	suspend fun validateAfterCreation(progressionStateId: Uuid): ProgressionStateException?
}