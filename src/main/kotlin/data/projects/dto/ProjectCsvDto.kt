package net.thechance.data.projects.dto

import data.projects.utils.ProjectColumnIndex.CREATED_BY
import data.projects.utils.ProjectColumnIndex.DESCRIPTION
import data.projects.utils.ProjectColumnIndex.ID
import data.projects.utils.ProjectColumnIndex.NAME
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
				id = fields[ID],
				name = fields[NAME],
				description = fields[DESCRIPTION],
				createdBy = fields[CREATED_BY]
			)
		}
	}
}
