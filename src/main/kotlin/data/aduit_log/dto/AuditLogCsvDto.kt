package net.thechance.data.aduit_log.dto

import logic.entities.EntityType
import net.thechance.data.aduit_log.utils.AuditLogColumnIndex.CREATEDAT
import net.thechance.data.aduit_log.utils.AuditLogColumnIndex.DESCRIPTION
import net.thechance.data.aduit_log.utils.AuditLogColumnIndex.ENTITYID
import net.thechance.data.aduit_log.utils.AuditLogColumnIndex.ENTITYTYPE
import net.thechance.data.aduit_log.utils.AuditLogColumnIndex.ID
import net.thechance.data.aduit_log.utils.AuditLogColumnIndex.USERNAME
import net.thechance.data.utils.CsvSerializable
import java.time.LocalDateTime
import java.util.*

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
