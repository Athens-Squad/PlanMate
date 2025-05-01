package logic.use_cases.state

import logic.repositories.StatesRepository
import net.thechance.logic.entities.State


class GetStateByIdUseCase(private val stateRepository: StatesRepository) {
    fun getStateById(stateId: String): State? {
        return null
    }
}