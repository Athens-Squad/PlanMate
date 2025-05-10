@file:OptIn(ExperimentalUuidApi::class)

package data.progression_state.data_source.localCsvFile.dto

import data.progression_state.utils.ProgressionStateColumIndex
import net.thechance.data.utils.csv_file_handle.CsvSerializable
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

data class ProgressionStateCsvDto(
	val id: Uuid = Uuid.random(),
	val name: String,
	val projectId: Uuid,
) : CsvSerializable {
	override fun toCsvFields(): List<String> = listOf(
		id.toString(),
		name,
		projectId.toString()
	)

	companion object {
		fun fromCsv(fields: List<String>): ProgressionStateCsvDto {
			return ProgressionStateCsvDto(
				id = Uuid.parse(fields[ProgressionStateColumIndex.ID]),
				name = fields[ProgressionStateColumIndex.NAME],
				projectId = Uuid.parse(fields[ProgressionStateColumIndex.PROJECT_ID])
			)
		}
	}
}