package logic.use_cases.state

import logic.repositories.StatesRepository
import logic.entities.ProgressionState


class GetStateByIdUseCase(private val stateRepository: StatesRepository) {
    fun execute(stateId: String): ProgressionState? {
        return null
    }
}