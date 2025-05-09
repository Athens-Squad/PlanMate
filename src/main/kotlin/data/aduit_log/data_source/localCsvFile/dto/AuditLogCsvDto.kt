package data.aduit_log.data_source.localCsvFile.dto

import logic.entities.EntityType
import net.thechance.data.aduit_log.utils.AuditLogColumnIndex
import net.thechance.data.utils.CsvSerializable
import java.time.LocalDateTime
import java.util.UUID

data class AuditLogCsvDto(
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
		fun fromCsv(fields: List<String>): AuditLogCsvDto {
			return AuditLogCsvDto(
				id = fields[AuditLogColumnIndex.ID],
				entityType = EntityType.valueOf(fields[AuditLogColumnIndex.ENTITYTYPE]),
				entityId = fields[AuditLogColumnIndex.ENTITYID],
				description = fields[AuditLogColumnIndex.DESCRIPTION],
				userName = fields[AuditLogColumnIndex.USERNAME],
				createdAt = LocalDateTime.parse(fields[AuditLogColumnIndex.CREATEDAT])
			)
		}
	}
}