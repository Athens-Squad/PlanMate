package logic.entities

import java.time.LocalDateTime
import java.util.*
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class AuditLog(
    val id: Uuid = Uuid.random(),
    val entityType: EntityType,
    val entityId: Uuid,
    val description: String,
    val userName: String,
    val createdAt: LocalDateTime
)