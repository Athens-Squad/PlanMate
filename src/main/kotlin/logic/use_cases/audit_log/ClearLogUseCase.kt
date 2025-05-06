package logic.use_cases.audit_log

import logic.repositories.AuditRepository

class ClearLogUseCase(private val auditRepository: AuditRepository) {
    suspend fun execute(): Any{
        return try {
            auditRepository.clearLog()
        } catch (e: Exception) {
            RuntimeException("Failed to clear logs")
        }
    }
}
