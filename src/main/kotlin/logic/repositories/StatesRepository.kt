package logic.repositories

import logic.entities.ProgressionState

interface StatesRepository {
    fun createState(progressionState: ProgressionState): Result<Unit>
    fun updateState(progressionState: ProgressionState): Result<Unit>
    fun deleteState(stateId: String): Result<Unit>
    fun getStates(): Result<List<ProgressionState>>
}