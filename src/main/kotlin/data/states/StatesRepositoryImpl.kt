package data.states

import logic.repositories.StatesRepository
import logic.entities.State

class StatesRepositoryImpl: StatesRepository {
    override fun createState(state: State): Result<Unit> {
        TODO("Not yet implemented")
    }

    override fun updateState(state: State): Result<Unit> {
        TODO("Not yet implemented")
    }

    override fun deleteState(stateId: String): Result<Unit> {
        TODO("Not yet implemented")
    }

    override fun getStates(): Result<List<State>> {
        TODO("Not yet implemented")
    }
}