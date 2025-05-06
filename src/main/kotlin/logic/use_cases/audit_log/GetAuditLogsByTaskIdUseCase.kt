package logic.use_cases.audit_log

import logic.entities.AuditLog
import logic.repositories.AuditRepository
import logic.entities.EntityType


class GetAuditLogsByTaskIdUseCase(private val auditRepository: AuditRepository) {
    suspend fun execute(taskId: String): List<AuditLog>{
        if (taskId.isBlank()) {
            return emptyList()
        }

        return try {
            auditRepository.getAuditLogs()
                .filter { it.entityType == EntityType.TASK && it.entityId == taskId }
        }catch(e: Exception){
            emptyList()
        }
    }
}