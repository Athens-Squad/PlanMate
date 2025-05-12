@file:OptIn(ExperimentalUuidApi::class)

package net.thechance.logic.use_cases.progression_state.progressionStateValidations

import logic.entities.ProgressionState
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

interface ProgressionStateValidator {
	fun validateProgressionStateFieldsNotBlank(progressionState: ProgressionState): Boolean
	suspend fun validateProjectExists(projectId: Uuid): Boolean
	suspend fun validateProgressionStateNotExists(progressionStateId: Uuid): Boolean
	suspend fun validateProgressionStateAlreadyExists(progressionStateId: Uuid): Boolean
}