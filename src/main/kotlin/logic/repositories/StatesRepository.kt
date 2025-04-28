package logic.repositories

import net.thechance.logic.entities.State

interface StatesRepository {
    fun createState(state: State): Result<Unit>
    fun updateState(state: State): Result<Unit>
    fun deleteState(stateId: String): Result<Unit>
    fun getStates(): Result<List<State>>
}