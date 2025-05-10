@file:OptIn(ExperimentalUuidApi::class)

package data.aduit_log.data_source.localCsvFile.dto

import logic.entities.EntityType
import net.thechance.data.aduit_log.utils.AuditLogColumnIndex
import net.thechance.data.utils.csv_file_handle.CsvSerializable
import java.time.LocalDateTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

data class AuditLogCsvDto(
	val id: Uuid = Uuid.random(),
	val entityType: EntityType,
	val entityId: Uuid,
	val description: String,
	val userName: String,
	val createdAt: LocalDateTime
): CsvSerializable {
	override fun toCsvFields(): List<String> = listOf(
		id.toString(),
		entityType.toString(),
		entityId.toString(),
		description,
		userName,
		createdAt.toString()
	)

	companion object {
		fun fromCsv(fields: List<String>): AuditLogCsvDto {
			return AuditLogCsvDto(
				id = Uuid.parse(fields[AuditLogColumnIndex.ID]),
				entityType = EntityType.valueOf(fields[AuditLogColumnIndex.ENTITYTYPE]),
				entityId = Uuid.parse(fields[AuditLogColumnIndex.ENTITYID]),
				description = fields[AuditLogColumnIndex.DESCRIPTION],
				userName = fields[AuditLogColumnIndex.USERNAME],
				createdAt = LocalDateTime.parse(fields[AuditLogColumnIndex.CREATEDAT])
			)
		}
	}
}