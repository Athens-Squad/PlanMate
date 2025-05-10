package logic.use_cases.audit_log

import logic.entities.AuditLog
import logic.repositories.AuditRepository
import net.thechance.logic.exceptions.MissingAuditLogFieldsException

class CreateAuditLogUseCase(private val auditRepository: AuditRepository) {
    suspend fun execute(auditLog: AuditLog) {
        if (auditLog.entityId.isBlank() || auditLog.description.isBlank() || auditLog.userName.isBlank()) {
          throw MissingAuditLogFieldsException()      }


        auditRepository.createAuditLog(auditLog)

    }
}