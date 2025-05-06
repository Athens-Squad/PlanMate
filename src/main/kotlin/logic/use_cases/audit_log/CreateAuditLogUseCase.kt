package logic.use_cases.audit_log

import logic.entities.AuditLog
import logic.repositories.AuditRepository

class CreateAuditLogUseCase(private val auditRepository: AuditRepository) {
    fun execute(auditLog: AuditLog) {
        if (auditLog.entityId.isBlank() || auditLog.description.isBlank() || auditLog.userName.isBlank()) {
            throw IllegalArgumentException("Invalid audit log: missing required fields")
        }


        auditRepository.createAuditLog(auditLog)

    }
}