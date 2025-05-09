package net.thechance.data.progression_state.dto

import data.progression_state.utils.ProgressionStateColumIndex.ID
import data.progression_state.utils.ProgressionStateColumIndex.NAME
import data.progression_state.utils.ProgressionStateColumIndex.PROJECT_ID
import net.thechance.data.utils.CsvSerializable
import java.util.*

data class ProgressionStateCsvDto(
	val id: String = UUID.randomUUID().toString(),
	val name: String,
	val projectId: String,
) : CsvSerializable {
	override fun toCsvFields(): List<String> = listOf(
		id,
		name,
		projectId
	)

	companion object {
		fun fromCsv(fields: List<String>): ProgressionStateCsvDto {
			return ProgressionStateCsvDto(
				id = fields[ID],
				name = fields[NAME],
				projectId = fields[PROJECT_ID]
			)
		}
	}
}
