package logic.use_cases.audit_log

import logic.entities.AuditLog
import logic.repositories.AuditRepository
import net.thechance.logic.validators.auditLogValidations.AuditLogValidator

class CreateAuditLogUseCase(
	private val auditRepository: AuditRepository,
	private val auditLogValidator: AuditLogValidator
	) {
    suspend fun execute(auditLog: AuditLog) {
		auditLogValidator.validateAuditLogFieldsNotBlank(auditLog)
        auditRepository.createAuditLog(auditLog)
    }
}