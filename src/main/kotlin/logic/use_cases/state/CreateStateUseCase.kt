package com.thechance.logic.usecases.state

import logic.entities.State
import logic.repositories.AuditRepository
import logic.repositories.StatesRepository

class CreateStateUseCase(
    private val stateRepository: StatesRepository,
    private val auditRepository: AuditRepository
) {
    fun execute(state: State): Boolean {
        return false
    }
}
