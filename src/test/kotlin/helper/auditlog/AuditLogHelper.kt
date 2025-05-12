@file:OptIn(ExperimentalUuidApi::class, ExperimentalUuidApi::class)

package helper.auditlog

import logic.entities.AuditLog
import logic.entities.EntityType
import java.time.LocalDateTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

fun createTestAuditLog(
    id: Uuid = Uuid.random(),
    entityType: EntityType = EntityType.PROJECT, // Replace with valid default
    entityId:Uuid= Uuid.random(),
    description: String = "Updated project status",
    userName: String = "test_user",
    createdAt: LocalDateTime = LocalDateTime.now()
): AuditLog {
    return AuditLog(
        id = id,
        entityType = entityType,
        entityId = entityId,
        description = description,
        userName = userName,
        createdAt = createdAt
    )
}