@file:OptIn(ExperimentalUuidApi::class)

package logic.use_cases.audit_log

import logic.entities.AuditLog
import logic.entities.EntityType
import logic.repositories.AuditRepository
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid


class GetAuditLogsByTaskIdUseCase(
	private val auditRepository: AuditRepository,
) {
    suspend fun execute(taskId: Uuid): List<AuditLog>{
        return try {
            auditRepository.getAuditLogs()
                .filter { it.entityType == EntityType.TASK && it.entityId == taskId }
        }catch(e: Exception){
            emptyList()
        }
    }
}