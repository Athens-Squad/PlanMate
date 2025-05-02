package logic.use_cases.state

import logic.repositories.AuditRepository
import logic.repositories.StatesRepository
import logic.entities.ProgressionState

class CreateStateUseCase(
    private val stateRepository: StatesRepository,
    private val auditRepository: AuditRepository
) {
    fun execute(progressionState: ProgressionState): Result<Unit> {
        return Result.success(Unit)
    }
}
