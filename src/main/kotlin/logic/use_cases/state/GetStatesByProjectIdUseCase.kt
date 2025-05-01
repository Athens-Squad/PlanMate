package logic.use_cases.state

import logic.repositories.StatesRepository
import logic.entities.ProgressionState


class GetStatesByProjectIdUseCase(private val stateRepository: StatesRepository) {
    fun execute(projectId: String): List<ProgressionState> {
        return emptyList()
    }
}
