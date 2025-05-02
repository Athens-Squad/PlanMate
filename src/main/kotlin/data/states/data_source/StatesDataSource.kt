package net.thechance.data.states.data_source

import net.thechance.logic.entities.State

interface StatesDataSource {

    fun createState(state: State): Result<Unit>

    fun updateState(state: State): Result<Unit>

    fun deleteState(stateId: String): Result<Unit>

    fun getStates(): Result<List<State>>
}