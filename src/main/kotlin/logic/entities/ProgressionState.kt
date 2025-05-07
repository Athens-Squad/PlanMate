package logic.entities

import logic.CsvSerializable
import java.util.*
import data.progression_state.utils.ProgressionStateColumIndex.ID
import data.progression_state.utils.ProgressionStateColumIndex.NAME
import data.progression_state.utils.ProgressionStateColumIndex.PROJECT_ID


data class ProgressionState(
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
		fun fromCsv(fields: List<String>): ProgressionState {
			return ProgressionState(
				id = fields[ID],
				name = fields[NAME],
				projectId = fields[PROJECT_ID]
			)
		}
	}
}