package com.thechance.logic.usecases.audit

import logic.entities.AuditLog
import logic.repositories.AuditRepository

class CreateAuditLogUseCase(private val auditRepository: AuditRepository) {
    fun execute(auditLog: AuditLog): Boolean {
        return false
    }
}