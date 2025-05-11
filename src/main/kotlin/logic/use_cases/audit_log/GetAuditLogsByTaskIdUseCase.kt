@file:OptIn(ExperimentalUuidApi::class)

package logic.use_cases.audit_log

import logic.entities.AuditLog
import logic.repositories.AuditRepository
import logic.entities.EntityType
import net.thechance.logic.use_cases.audit_log.auditLogValidations.AuditLogValidator
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid


class GetAuditLogsByTaskIdUseCase(
	private val auditRepository: AuditRepository,
	private val auditLogValidator: AuditLogValidator
) {
    suspend fun execute(taskId: Uuid): List<AuditLog>{
		auditLogValidator.validateAfterCreation(taskId)
        return try {
            auditRepository.getAuditLogs()
                .filter { it.entityType == EntityType.TASK && it.entityId == taskId }
        }catch(e: Exception){
            emptyList()
        }
    }
}