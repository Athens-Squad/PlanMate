@file:OptIn(ExperimentalUuidApi::class)

package logic.use_cases.audit_log

import logic.entities.AuditLog
import logic.entities.EntityType
import logic.repositories.AuditRepository
import net.thechance.logic.use_cases.audit_log.auditLogValidations.AuditLogValidator
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class GetAuditLogsByProjectIdUseCase(
	private val auditRepository: AuditRepository,
	private val auditLogValidator: AuditLogValidator
) {
    suspend fun execute(projectId: Uuid): List<AuditLog> {
        auditLogValidator.validateAfterCreation(projectId)
        return try {
            auditRepository.getAuditLogs()
                .filter { it.entityType == EntityType.PROJECT && it.entityId == projectId }
        } catch (e: Exception) {
            emptyList()
        }
    }
}
