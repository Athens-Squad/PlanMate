package logic.use_cases.audit_log

import logic.entities.AuditLog
import logic.entities.EntityType
import logic.repositories.AuditRepository
import net.thechance.logic.use_cases.audit_log.auditLogValidations.AuditLogValidator

class GetAuditLogsByProjectIdUseCase(
	private val auditRepository: AuditRepository,
	private val auditLogValidator: AuditLogValidator
) {
    suspend fun execute(projectId: String): List<AuditLog> {
        auditLogValidator.validateAfterCreation(projectId)
        return try {
            auditRepository.getAuditLogs()
                .filter { it.entityType == EntityType.PROJECT && it.entityId == projectId }
        } catch (e: Exception) {
            emptyList()
        }
    }
}
