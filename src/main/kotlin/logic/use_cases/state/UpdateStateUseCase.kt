package logic.use_cases.state

import logic.repositories.AuditRepository
import logic.repositories.StatesRepository
import net.thechance.logic.entities.State


class UpdateStateUseCase(
    private val stateRepository: StatesRepository
) {
    fun updateState(stateId: String, updatedState: State): Boolean {
        return false
    }
}
