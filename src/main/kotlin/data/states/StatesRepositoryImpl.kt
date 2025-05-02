package data.states

import logic.repositories.StatesRepository
import logic.entities.ProgressionState

class StatesRepositoryImpl: StatesRepository {
    override fun createState(progressionState: ProgressionState): Result<Unit> {
        TODO("Not yet implemented")
    }

    override fun updateState(progressionState: ProgressionState): Result<Unit> {
        TODO("Not yet implemented")
    }

    override fun deleteState(stateId: String): Result<Unit> {
        TODO("Not yet implemented")
    }

    override fun getStates(): Result<List<ProgressionState>> {
        TODO("Not yet implemented")
    }
}