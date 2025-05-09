package net.thechance.data.tasks.dto

import data.tasks.utils.TaskColumnIndex.CURRENT_STATE_ID
import data.tasks.utils.TaskColumnIndex.CURRENT_STATE_NAME
import data.tasks.utils.TaskColumnIndex.DESCRIPTION
import data.tasks.utils.TaskColumnIndex.ID
import data.tasks.utils.TaskColumnIndex.PROJECT_ID
import data.tasks.utils.TaskColumnIndex.TITLE
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
				id = fields[ID],
				title = fields[TITLE],
				description = fields[DESCRIPTION],
				currentProgressionState = ProgressionState(
					id = fields[CURRENT_STATE_ID],
					name = fields[CURRENT_STATE_NAME],
					projectId = fields[PROJECT_ID]
				),
				projectId = fields[PROJECT_ID]
			)
		}
	}

}