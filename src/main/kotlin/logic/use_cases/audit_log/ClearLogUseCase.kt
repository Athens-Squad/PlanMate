package logic.use_cases.audit_log

import logic.repositories.AuditRepository

class ClearLogUseCase(private val auditRepository: AuditRepository) {
    fun execute():Result<Unit>{

         return  auditRepository.clearLog()
             .onFailure {throwable ->
                 throw RuntimeException("Failed to clear logs", throwable)
             }

}}