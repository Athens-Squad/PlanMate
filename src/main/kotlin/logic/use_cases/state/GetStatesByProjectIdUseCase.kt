package logic.use_cases.state

import logic.repositories.StatesRepository
import logic.entities.ProgressionState


class GetStatesByProjectIdUseCase(private val stateRepository: StatesRepository) {
    fun execute(projectId: String):Result<List<ProgressionState>> {
        return Result.success(emptyList())
    }
}
