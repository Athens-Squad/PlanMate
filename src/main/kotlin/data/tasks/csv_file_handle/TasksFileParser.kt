package net.thechance.data.tasks.csv_file_handle

import logic.entities.Task
import net.thechance.data.tasks.utils.TaskColumnIndex
import net.thechance.logic.entities.State

class TasksFileParser {

    fun parseRecord(record: String):Task {
        val fields = record.split(",")
            .map { it.trim() }

        return Task(
            id = fields[TaskColumnIndex.ID],
            title = fields[TaskColumnIndex.TITLE],
            description = fields[TaskColumnIndex.DESCRIPTION],
            currentState = State(
                id = fields[TaskColumnIndex.CURRENT_STATE_ID],
                name = fields[TaskColumnIndex.CURRENT_STATE_NAME],
                projectId = fields[TaskColumnIndex.PROJECT_ID]
            ),
            projectId = fields[TaskColumnIndex.PROJECT_ID]
        )
    }

    fun taskToCsvRecord(task: Task): String {
        return task.run {
            listOf(
                id,
                title,
                description,
                currentState.id,
                currentState.name,
                projectId
            )
        }.joinToString(",")
    }
}