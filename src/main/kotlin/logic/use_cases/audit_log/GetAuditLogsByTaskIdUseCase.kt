package net.thechance.logic.use_cases.audit_log

import logic.entities.AuditLog
import logic.repositories.AuditRepository
import net.thechance.logic.entities.EntityType


class GetAuditLogsByTaskIdUseCase(private val auditRepository: AuditRepository) {
    fun execute(taskId: String): List<AuditLog> {
        if (taskId.isBlank()) {
            return emptyList()
        }

        return try {
            auditRepository.getAuditLogs()
                .filter {  it.entityId==taskId && it.entityType== EntityType.TASK  }
        } catch (e: Exception) {
            emptyList()
        }
    }
}