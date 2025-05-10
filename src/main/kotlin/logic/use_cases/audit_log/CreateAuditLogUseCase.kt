@file:OptIn(ExperimentalUuidApi::class)

package logic.use_cases.audit_log

import logic.entities.AuditLog
import logic.repositories.AuditRepository
import kotlin.uuid.ExperimentalUuidApi

class CreateAuditLogUseCase(private val auditRepository: AuditRepository) {
    suspend fun execute(auditLog: AuditLog) {
        if (auditLog.entityId.toString().isBlank() || auditLog.description.isBlank() || auditLog.userName.isBlank()) {
            throw IllegalArgumentException("Invalid audit log: missing required fields")
        }


        auditRepository.createAuditLog(auditLog)

    }
}