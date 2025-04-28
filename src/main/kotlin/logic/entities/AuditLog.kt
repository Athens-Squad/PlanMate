package logic.entities


import net.thechance.logic.entities.EntityType
import java.time.LocalDateTime
import java.util.UUID

data class AuditLog(
    val id: String = UUID.randomUUID().toString(),
    val entityType: EntityType,
    val entityId: String,
    val description: String,
    val userId: String,
    val createdAt: LocalDateTime
)

