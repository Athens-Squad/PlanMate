package logic.use_cases.audit_log

import logic.entities.AuditLog
import logic.entities.EntityType
import logic.repositories.AuditRepository

class GetAuditLogsByProjectIdUseCase(private val auditRepository: AuditRepository) {
    suspend fun execute(projectId: String): List<AuditLog> {
        if (projectId.isBlank()) {
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
