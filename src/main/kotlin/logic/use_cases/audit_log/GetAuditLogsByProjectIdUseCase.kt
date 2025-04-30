package net.thechance.logic.use_cases.audit_log

import logic.entities.AuditLog
import logic.repositories.AuditRepository
import net.thechance.logic.entities.EntityType


class GetAuditLogsByProjectIdUseCase(private val auditRepository: AuditRepository) {
    fun execute(projectId: String):Result< List<AuditLog>> {
        if (projectId.isBlank()) {
            return Result.success(emptyList())
        }

        return kotlin.runCatching { auditRepository.getAuditLogs()
            .filter { it.entityType==EntityType.PROJECT && it.entityId==projectId }}
            .recoverCatching {
                //throwable->
                  //throw RuntimeException("Failed to retrieve audit logs", throwable)
                   emptyList()
            }


    }
    }
