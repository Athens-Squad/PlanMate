package data.progression_state.data_source.localCsvFile.dto

import data.progression_state.utils.ProgressionStateColumIndex
import net.thechance.data.utils.CsvSerializable
import java.util.UUID

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
				id = fields[ProgressionStateColumIndex.ID],
				name = fields[ProgressionStateColumIndex.NAME],
				projectId = fields[ProgressionStateColumIndex.PROJECT_ID]
			)
		}
	}
}