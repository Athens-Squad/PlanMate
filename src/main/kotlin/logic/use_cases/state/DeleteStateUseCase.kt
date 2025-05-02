package logic.use_cases.state

import logic.repositories.AuditRepository
import logic.repositories.StatesRepository

class DeleteStateUseCase(
    private val stateRepository: StatesRepository,
    private val auditRepository: AuditRepository
) {
    fun execute(stateId: String): Result<Unit> {
        return Result.success(Unit)
    }
}