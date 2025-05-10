@file:OptIn(ExperimentalUuidApi::class)

package logic.use_cases.audit_log

import logic.entities.AuditLog
import logic.entities.EntityType
import logic.repositories.AuditRepository
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class GetAuditLogsByProjectIdUseCase(private val auditRepository: AuditRepository) {
    suspend fun execute(projectId: Uuid): List<AuditLog> {
        if (projectId.toString().isBlank()) {
            return emptyList()
        }

        return try {
            auditRepository.getAuditLogs()
                .filter { it.entityType == EntityType.PROJECT && it.entityId == projectId }
        } catch (e: Exception) {
            emptyList()
        }
    }
}
