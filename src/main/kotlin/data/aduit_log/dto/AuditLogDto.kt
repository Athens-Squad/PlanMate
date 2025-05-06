package net.thechance.data.aduit_log.dto

import logic.entities.AuditLog
import logic.entities.EntityType
import org.bson.codecs.pojo.annotations.BsonId
import java.time.LocalDateTime
import java.util.*

data class AuditLogDto(
    @BsonId val id: String = UUID.randomUUID().toString(),
    val entityType: String,
    val entityId: String,
    val description: String,
    val userName: String,
    val createdAt: String
) {
    fun toAuditLog() = AuditLog(
        id = id,
        entityType = EntityType.valueOf(entityType),
        entityId = entityId,
        description = description,
        userName = userName,
        createdAt = LocalDateTime.parse(createdAt)
    )

    companion object {
        fun fromAuditLog(auditLog: AuditLog) = AuditLogDto(
            id = auditLog.id,
            entityType = auditLog.entityType.name,
            entityId = auditLog.entityId,
            description = auditLog.description,
            userName = auditLog.userName,
            createdAt = auditLog.createdAt.toString()
        )
    }
}
