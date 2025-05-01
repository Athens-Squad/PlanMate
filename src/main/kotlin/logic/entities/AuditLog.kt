package logic.entities


import net.thechance.data.aduit_log_csvfile.utils.AuditLogColumnIndex.CREATEDAT
import net.thechance.data.aduit_log_csvfile.utils.AuditLogColumnIndex.ENTITYID
import net.thechance.data.aduit_log_csvfile.utils.AuditLogColumnIndex.ENTITYTYPE
import net.thechance.data.aduit_log_csvfile.utils.AuditLogColumnIndex.USERNAME
import net.thechance.data.aduit_log_csvfile.utils.AuditLogColumnIndex.DESCRIPTION
import net.thechance.data.aduit_log_csvfile.utils.AuditLogColumnIndex.ID
import net.thechance.logic.CsvSerializable
import net.thechance.logic.entities.EntityType
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