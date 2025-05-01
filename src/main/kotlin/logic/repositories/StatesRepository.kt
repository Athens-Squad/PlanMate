package logic.repositories

import net.thechance.logic.entities.State

interface StatesRepository {
    fun createStateInFile(state: State): Result<Unit>
    fun updateStateInFile(state: State): Result<Unit>
    fun deleteStateFromFile(stateId: String): Result<Unit>
    fun getStatesFromFile(): Result<List<State>>
}