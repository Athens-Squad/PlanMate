package logic.use_cases.state

import logic.repositories.AuditRepository
import logic.repositories.StatesRepository
import logic.entities.ProgressionState


class UpdateStateUseCase(
    private val stateRepository: StatesRepository,
    private val auditRepository: AuditRepository
) {
    fun execute(stateId: String, updatedProgressionState: ProgressionState): Boolean {
        return false
    }
}
