package net.thechance.logic.use_cases.audit_log

import logic.entities.AuditLog
import logic.repositories.AuditRepository


class GetAuditLogsByProjectIdUseCase(private val auditRepository: AuditRepository) {
    fun execute(projectId: String): List<AuditLog> {
        return emptyList()
    }
}