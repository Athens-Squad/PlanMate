package net.thechance.logic.use_cases.audit_log

import logic.entities.AuditLog
import logic.repositories.AuditRepository
import net.thechance.logic.entities.EntityType


class GetAuditLogsByProjectIdUseCase(private val auditRepository: AuditRepository) {
    fun execute(projectId: String):Result< List<AuditLog>> {
        if (projectId.isBlank()) {
            return Result.success(emptyList())
        }

        return auditRepository.getAuditLogs()
                .mapCatching { auditLogs ->
                    auditLogs.filter { it.entityType == EntityType.PROJECT && it.entityId == projectId }
                }
                .recoverCatching {
                    emptyList()
                }
        }
    }