package logic.entities

import net.thechance.data.tasks.utils.TaskColumnIndex.CURRENT_STATE_ID
import net.thechance.data.tasks.utils.TaskColumnIndex.CURRENT_STATE_NAME
import net.thechance.data.tasks.utils.TaskColumnIndex.DESCRIPTION
import net.thechance.data.tasks.utils.TaskColumnIndex.ID
import net.thechance.data.tasks.utils.TaskColumnIndex.PROJECT_ID
import net.thechance.data.tasks.utils.TaskColumnIndex.TITLE
import net.thechance.logic.entities.State
import net.thechance.logic.CsvSerializable
import java.util.UUID


data class Task(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val description: String,
    val currentState: State,
    val projectId: String
) : CsvSerializable {
    override fun toCsvFields(): List<String> = listOf(
        id,
        title,
        description,
        currentState.id,
        currentState.name,
        projectId
    )

    companion object {
        fun fromCsv(fields: List<String>): Task {
            return Task(
                id = fields[ID],
                title = fields[TITLE],
                description = fields[DESCRIPTION],
                currentState = State(
                    id = fields[CURRENT_STATE_ID],
                    name = fields[CURRENT_STATE_NAME],
                    projectId = fields[PROJECT_ID]
                ),
                projectId = fields[PROJECT_ID]
            )
        }
    }

}