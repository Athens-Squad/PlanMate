package logic.use_cases.state

import logic.repositories.StatesRepository
import logic.entities.State


class GetStatesByProjectIdUseCase(private val stateRepository: StatesRepository) {
    fun execute(projectId: String): List<State> {
        return emptyList()
    }
}
