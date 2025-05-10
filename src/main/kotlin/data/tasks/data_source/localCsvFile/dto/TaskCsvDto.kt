@file:OptIn(ExperimentalUuidApi::class)

package net.thechance.data.tasks.data_source.localCsvFile.dto

import data.tasks.utils.TaskColumnIndex
import logic.entities.ProgressionState
import net.thechance.data.utils.csv_file_handle.CsvSerializable
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

data class TaskCsvDto(
	val id: Uuid = Uuid.random(),
	val title: String,
	val description: String,
	val currentProgressionState: ProgressionState,
	val projectId: Uuid
) : CsvSerializable {
	override fun toCsvFields(): List<String> = listOf(
		id.toString(),
		title,
		description,
		currentProgressionState.id.toString(),
		currentProgressionState.name,
		projectId.toString()
	)

	companion object {
		fun fromCsv(fields: List<String>): TaskCsvDto {
			return TaskCsvDto(
				id = Uuid.parse(fields[TaskColumnIndex.ID]),
				title = fields[TaskColumnIndex.TITLE],
				description = fields[TaskColumnIndex.DESCRIPTION],
				currentProgressionState = ProgressionState(
					id = Uuid.parse(fields[TaskColumnIndex.CURRENT_STATE_ID]),
					name = fields[TaskColumnIndex.CURRENT_STATE_NAME],
					projectId = Uuid.parse(fields[TaskColumnIndex.PROJECT_ID])
				),
				projectId = Uuid.parse(fields[TaskColumnIndex.PROJECT_ID])
			)
		}
	}

}