package logic.use_cases.state

import logic.repositories.StatesRepository
import net.thechance.logic.entities.State


class GetStateByIdUseCase(private val stateRepository: StatesRepository) {
    fun execute(stateId: String): State {

       return stateRepository.getStates().getOrThrow().find { it.id == stateId }
            ?: throw IllegalArgumentException("Project with ID $stateId does not exist.")

    }
}