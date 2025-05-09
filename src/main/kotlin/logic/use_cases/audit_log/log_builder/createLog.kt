package net.thechance.logic.use_cases.audit_log.log_builder

import logic.entities.AuditLog
import logic.entities.EntityType
import logic.entities.Project
import java.time.LocalDateTime

suspend fun createLog(
    entityType: EntityType,
    entityId: String,
    logMessage: String,
    userName: String,
    action: suspend (auditLog: AuditLog) -> Unit
) {
    val auditLog = AuditLog(
        entityType = entityType,
        entityId = entityId,
        description = logMessage,
        userName = userName,
        createdAt = LocalDateTime.now()
    )
    action(auditLog)
}