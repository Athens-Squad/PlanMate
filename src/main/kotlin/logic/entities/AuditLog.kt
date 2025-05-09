package logic.entities

import java.time.LocalDateTime
import java.util.*

data class AuditLog(
    val id: String = UUID.randomUUID().toString(),
    val entityType: EntityType,
    val entityId: String,
    val description: String,
    val userName: String,
    val createdAt: LocalDateTime
)