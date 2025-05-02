package data.states.data_source

import logic.entities.ProgressionState

interface StatesDataSource {

    fun createState(state: ProgressionState): Result<Unit>

    fun updateState(state: ProgressionState): Result<Unit>

    fun deleteState(stateId: String): Result<Unit>

    fun getStates(): Result<List<ProgressionState>>
}