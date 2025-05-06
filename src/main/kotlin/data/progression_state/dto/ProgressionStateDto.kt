package net.thechance.data.progression_state.dto

import data.progression_state.utils.ProgressionStateColumIndex.ID
import data.progression_state.utils.ProgressionStateColumIndex.NAME
import data.progression_state.utils.ProgressionStateColumIndex.PROJECT_ID
import logic.CsvSerializable
import logic.entities.ProgressionState
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.codecs.pojo.annotations.BsonProperty
import java.util.UUID

data class ProgressionStateDto(
	@BsonId
	val id: String = UUID.randomUUID().toString(),
	val name: String,
	@BsonProperty("created_by")
	val projectId: String,
): CsvSerializable {
	override fun toCsvFields(): List<String> = listOf(
		id,
		name,
		projectId
	)

	companion object {
		fun fromCsv(fields: List<String>): ProgressionStateDto {
			return ProgressionStateDto(
				id = fields[ID],
				name = fields[NAME],
				projectId = fields[PROJECT_ID]
			)
		}
	}
}
