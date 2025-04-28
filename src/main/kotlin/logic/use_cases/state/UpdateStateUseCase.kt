package com.thechance.logic.usecases.state

import logic.entities.State
import logic.repositories.AuditRepository
import logic.repositories.StatesRepository


class UpdateStateUseCase(
    private val stateRepository: StatesRepository,
    private val auditRepository: AuditRepository
) {
    fun execute(stateId: String, updatedState: State): Boolean {
        return false
    }
}
