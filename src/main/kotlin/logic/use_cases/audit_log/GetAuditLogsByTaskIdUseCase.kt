package net.thechance.logic.use_cases.audit_log

import logic.entities.AuditLog
import logic.repositories.AuditRepository
import net.thechance.logic.entities.EntityType


class GetAuditLogsByTaskIdUseCase(private val auditRepository: AuditRepository) {
    fun execute(taskId: String): Result<List<AuditLog>> {
        if (taskId.isBlank()) {
            return Result.success(emptyList())
        }

        return auditRepository.getAuditLogs()
            .mapCatching { auditLog ->
                auditLog.filter { it.entityType == EntityType.TASK && it.entityId == taskId }
            }.recoverCatching {
                emptyList()
            }

    }
}