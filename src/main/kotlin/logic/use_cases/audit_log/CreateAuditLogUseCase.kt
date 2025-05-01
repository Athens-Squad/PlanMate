package logic.use_cases.audit_log

import logic.entities.AuditLog
import logic.repositories.AuditRepository

class CreateAuditLogUseCase(private val auditRepository: AuditRepository) {
    fun execute(auditLog: AuditLog): Result<Unit> {
        if (auditLog.entityId.isBlank() || auditLog.description.isBlank() || auditLog.userName.isBlank()) {
            return Result.failure(
                IllegalArgumentException("Invalid audit log: missing required fields")
            )

        }

        return runCatching {
            auditRepository.createAuditLog(auditLog)
                .onFailure { throwable ->
                    throw RuntimeException("Failed to create audit log", throwable)
                }
        }
    }
}