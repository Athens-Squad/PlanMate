package com.thechance.logic.usecases.state

import logic.repositories.AuditRepository
import logic.repositories.StatesRepository

class DeleteStateUseCase(
    private val stateRepository: StatesRepository,
    private val auditRepository: AuditRepository
) {
    fun execute(stateId: String): Boolean {
        return false
    }
}