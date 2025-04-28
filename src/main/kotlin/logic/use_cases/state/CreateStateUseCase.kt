package logic.use_cases.state

import logic.repositories.AuditRepository
import logic.repositories.StatesRepository
import net.thechance.logic.entities.State

class CreateStateUseCase(
    private val stateRepository: StatesRepository,
    private val auditRepository: AuditRepository
) {
    fun execute(state: State): Boolean {
        return false
    }
}
