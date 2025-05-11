package logic.use_cases.audit_log

import logic.entities.AuditLog
import logic.repositories.AuditRepository

class CreateAuditLogUseCase(
	private val auditRepository: AuditRepository,
	private val auditLogValidator: AuditLogValidator
	) {
    suspend fun execute(auditLog: AuditLog) {
		auditLogValidator.validateBeforeCreation(auditLog)
        auditRepository.createAuditLog(auditLog)
    }
}