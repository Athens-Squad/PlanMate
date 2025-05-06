package logic.use_cases.audit_log

import logic.entities.AuditLog
import logic.repositories.AuditRepository

class CreateAuditLogUseCase(private val auditRepository: AuditRepository) {
    suspend fun execute(auditLog: AuditLog): Any {
        if (auditLog.entityId.isBlank() || auditLog.description.isBlank() || auditLog.userName.isBlank()) {
            return IllegalArgumentException("Invalid audit log: missing required fields")
        }

        try {
            return auditRepository.createAuditLog(auditLog)
        }catch (e: Exception){
            throw RuntimeException("Failed to create audit log")
        }

    }
}