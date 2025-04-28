package net.thechance.logic.use_cases.audit_log

import logic.entities.AuditLog
import logic.repositories.AuditRepository


class GetAuditLogsByTaskIdUseCase(private val auditRepository: AuditRepository) {
    fun execute(taskId: String): List<AuditLog> {
        return emptyList()
    }
}