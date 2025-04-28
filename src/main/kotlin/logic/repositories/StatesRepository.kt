package logic.repositories

import net.thechance.logic.entities.State

interface StatesRepository {
    fun createState(state: State)
    fun updateState(state: State)
    fun deleteState(stateId: String)
    fun getStates(): List<State>
}