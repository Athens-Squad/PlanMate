package net.thechance.logic.use_cases.audit_log

import logic.entities.AuditLog
import logic.repositories.AuditRepository
import net.thechance.logic.entities.EntityType


class GetAuditLogsByProjectIdUseCase(private val auditRepository: AuditRepository) {
    fun execute(projectId: String): List<AuditLog> {
        if (projectId.isBlank()) {
            return emptyList()
        }

        return try {
            auditRepository.getAuditLogs()
                .filter { it.entityType==EntityType.PROJECT && it.entityId==projectId }
        } catch (e: Exception) {
            emptyList()
        }
    }
    }
