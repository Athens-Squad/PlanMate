package data.aduit_log_csvfile

import logic.entities.AuditLog
import net.thechance.logic.entities.EntityType
import java.time.LocalDateTime

fun dummyAuditLog(): AuditLog = AuditLog(
    id = "123",
    entityType = EntityType.PROJECT,
    entityId = "proj-1",
    description = "Project created",
    userName = "admin",
    createdAt = LocalDateTime.now()
)