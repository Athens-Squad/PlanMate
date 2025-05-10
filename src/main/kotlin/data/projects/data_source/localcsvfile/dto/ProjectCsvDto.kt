@file:OptIn(ExperimentalUuidApi::class)

package data.projects.data_source.localcsvfile.dto

import data.projects.utils.ProjectColumnIndex
import logic.entities.ProgressionState
import logic.entities.Task
import net.thechance.data.utils.csv_file_handle.CsvSerializable
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

data class ProjectCsvDto(
	val id: Uuid = Uuid.random(),
	val name: String,
	val description: String,
	val progressionStates: MutableList<ProgressionState> = mutableListOf(),
	val tasks: MutableList<Task> = mutableListOf(),
	val createdByUserName: String
) : CsvSerializable {
	override fun toCsvFields(): List<String> = listOf(
		id.toString(),
		name,
		description,
		createdByUserName
	)

	companion object {
		fun fromCsv(fields: List<String>): ProjectCsvDto {
			return ProjectCsvDto(
				id = Uuid.parse(fields[ProjectColumnIndex.ID]),
				name = fields[ProjectColumnIndex.NAME],
				description = fields[ProjectColumnIndex.DESCRIPTION],
				createdByUserName = fields[ProjectColumnIndex.CREATED_BY]
			)
		}
	}
}