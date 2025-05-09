package net.thechance.data.tasks.data_source.localCsvFile.dto

import data.tasks.utils.TaskColumnIndex
import logic.entities.ProgressionState
import net.thechance.data.utils.CsvSerializable
import java.util.UUID

data class TaskCsvDto(
	val id: String = UUID.randomUUID().toString(),
	val title: String,
	val description: String,
	val currentProgressionState: ProgressionState,
	val projectId: String
) : CsvSerializable {
	override fun toCsvFields(): List<String> = listOf(
		id,
		title,
		description,
		currentProgressionState.id,
		currentProgressionState.name,
		projectId
	)

	companion object {
		fun fromCsv(fields: List<String>): TaskCsvDto {
			return TaskCsvDto(
				id = fields[TaskColumnIndex.ID],
				title = fields[TaskColumnIndex.TITLE],
				description = fields[TaskColumnIndex.DESCRIPTION],
				currentProgressionState = ProgressionState(
					id = fields[TaskColumnIndex.CURRENT_STATE_ID],
					name = fields[TaskColumnIndex.CURRENT_STATE_NAME],
					projectId = fields[TaskColumnIndex.PROJECT_ID]
				),
				projectId = fields[TaskColumnIndex.PROJECT_ID]
			)
		}
	}

}