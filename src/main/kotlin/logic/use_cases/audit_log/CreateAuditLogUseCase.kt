package logic.use_cases.audit_log

import logic.entities.AuditLog
import logic.repositories.AuditRepository

class CreateAuditLogUseCase(private val auditRepository: AuditRepository) {
    fun execute(auditLog: AuditLog): Boolean {
        if (auditLog.entityId.isBlank() || auditLog.description.isBlank() || auditLog.userId.isBlank()) {
            return false
        }
        try {
            auditRepository.createAuditLog(auditLog)
            return true
        } catch (e: Exception) {
            return false
        }
    }
}