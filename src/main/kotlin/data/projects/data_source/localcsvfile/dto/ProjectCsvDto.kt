package data.projects.data_source.localcsvfile.dto

import data.projects.utils.ProjectColumnIndex
import logic.entities.ProgressionState
import logic.entities.Task
import net.thechance.data.utils.CsvSerializable
import java.util.UUID

data class ProjectCsvDto(
	val id: String = UUID.randomUUID().toString(),
	val name: String,
	val description: String,
	val progressionStates: MutableList<ProgressionState> = mutableListOf(),
	val tasks: MutableList<Task> = mutableListOf(),
	val createdBy: String
) : CsvSerializable {
	override fun toCsvFields(): List<String> = listOf(
		id,
		name,
		description,
		createdBy
	)

	companion object {
		fun fromCsv(fields: List<String>): ProjectCsvDto {
			return ProjectCsvDto(
				id = fields[ProjectColumnIndex.ID],
				name = fields[ProjectColumnIndex.NAME],
				description = fields[ProjectColumnIndex.DESCRIPTION],
				createdBy = fields[ProjectColumnIndex.CREATED_BY]
			)
		}
	}
}