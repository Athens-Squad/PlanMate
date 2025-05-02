package logic.use_cases.state

import logic.repositories.StatesRepository
import net.thechance.logic.entities.State


class GetStatesByProjectIdUseCase(private val stateRepository: StatesRepository) {
    fun execute(projectId: String): List<State> {

        return stateRepository.getStates().getOrThrow().filter { it.projectId == projectId }
    }
}
