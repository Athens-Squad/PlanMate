package logic.use_cases.audit_log

import logic.repositories.AuditRepository

class ClearLogUseCase(private val auditRepository: AuditRepository) {
    fun execute() {
         auditRepository.clearLog()
    }
}