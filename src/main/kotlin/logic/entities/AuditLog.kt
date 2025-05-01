package logic.entities

import data.aduit_log_csvfile.utils.AuditLogColumnIndex.CREATEDAT
import data.aduit_log_csvfile.utils.AuditLogColumnIndex.ENTITYID
import data.aduit_log_csvfile.utils.AuditLogColumnIndex.ENTITYTYPE
import data.aduit_log_csvfile.utils.AuditLogColumnIndex.USERNAME
import data.aduit_log_csvfile.utils.AuditLogColumnIndex.DESCRIPTION
import data.aduit_log_csvfile.utils.AuditLogColumnIndex.ID
import logic.CsvSerializable
import java.time.LocalDateTime
import java.util.UUID

data class AuditLog(
    val id: String = UUID.randomUUID().toString(),
    val entityType: EntityType,
    val entityId: String,
    val description: String,
    val userName: String,
    val createdAt: LocalDateTime
): CsvSerializable {
    override fun toCsvFields(): List<String> = listOf(
        id,
        entityType.toString(),
        entityId,
        description,
        userName,
        createdAt.toString()
    )

    companion object {
        fun fromCsv(fields: List<String>): AuditLog {
            return AuditLog(
                id = fields[ID],
                entityType = EntityType.valueOf(fields[ENTITYTYPE]),
                entityId = fields[ENTITYID],
                description = fields[DESCRIPTION],
                userName = fields[USERNAME],
                createdAt = LocalDateTime.parse(fields[CREATEDAT])
            )
        }
    }
}