@file:OptIn(ExperimentalUuidApi::class)

package data.auditlog

import logic.entities.AuditLog
import logic.entities.EntityType
import java.time.LocalDateTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

fun dummyAuditLog(): AuditLog = AuditLog(
    id = Uuid.random(),
    entityType = EntityType.PROJECT,
    entityId = Uuid.random(),
    description = "Project created",
    userName = "admin",
    createdAt = LocalDateTime.now()
)