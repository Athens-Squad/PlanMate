@file:OptIn(ExperimentalUuidApi::class)

package logic.use_cases.audit_log

import logic.entities.AuditLog
import logic.repositories.AuditRepository
import logic.entities.EntityType
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid


class GetAuditLogsByTaskIdUseCase(private val auditRepository: AuditRepository) {
    suspend fun execute(taskId: Uuid): List<AuditLog>{
        if (taskId.toString().isBlank()) {
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