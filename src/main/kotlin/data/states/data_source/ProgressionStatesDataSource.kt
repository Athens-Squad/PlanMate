package data.states.data_source

import logic.entities.ProgressionState

interface ProgressionStatesDataSource {

    fun createProgressionState(progressionState: ProgressionState): Result<Unit>

    fun updateState(progressionState: ProgressionState): Result<Unit>

    fun deleteState(stateId: String): Result<Unit>

    fun getStates(): Result<List<ProgressionState>>
}