package logic.use_cases.state

import logic.repositories.AuditRepository
import logic.repositories.StatesRepository
import logic.entities.ProgressionState


class UpdateStateUseCase(
    private val stateRepository: StatesRepository,
    private val auditRepository: AuditRepository
) {
    fun execute(state: ProgressionState, updatedState: ProgressionState): Result<Unit> {
        return Result.success(Unit)
    }
}
