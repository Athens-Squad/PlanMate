package net.thechance.logic.entities


import logic.entities.Task
import net.thechance.data.states.Utils.StateColumIndex.NAME
import net.thechance.data.tasks.utils.TaskColumnIndex.CURRENT_STATE_ID
import net.thechance.data.tasks.utils.TaskColumnIndex.CURRENT_STATE_NAME
import net.thechance.data.tasks.utils.TaskColumnIndex.DESCRIPTION
import net.thechance.data.tasks.utils.TaskColumnIndex.ID
import net.thechance.data.tasks.utils.TaskColumnIndex.PROJECT_ID
import net.thechance.data.tasks.utils.TaskColumnIndex.TITLE
import net.thechance.logic.CsvSerializable
import java.util.UUID


data class State(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val projectId: String,
): CsvSerializable {
    override fun toCsvFields(): List<String> = listOf(
        id,
        name,
        projectId
    )

    companion object {
        fun fromCsv(fields: List<String>): State {
            return State(
                id = fields[ID],
                name = fields[NAME],
                projectId = fields[PROJECT_ID]
            )
        }
    }
}
