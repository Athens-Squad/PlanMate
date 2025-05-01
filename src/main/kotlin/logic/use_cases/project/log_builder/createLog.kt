package net.thechance.logic.use_cases.project.log_builder

import logic.entities.AuditLog
import logic.entities.Project
import net.thechance.logic.entities.EntityType
import java.time.LocalDateTime

fun createLog(project: Project, logMessage: String, action: (auditLog: AuditLog) -> Unit) {
    val auditLog = AuditLog(
        entityType = EntityType.PROJECT,
        entityId = project.id,
        description = logMessage,
        userName = project.createdBy,
        createdAt = LocalDateTime.now()
    )
    action(auditLog)
}