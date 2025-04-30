package logic.entities

import net.thechance.data.tasks.utils.TaskColumnIndex.CURRENT_STATE_ID
import net.thechance.data.tasks.utils.TaskColumnIndex.CURRENT_STATE_NAME
import net.thechance.data.tasks.utils.TaskColumnIndex.DESCRIPTION
import net.thechance.data.tasks.utils.TaskColumnIndex.ID
import net.thechance.data.tasks.utils.TaskColumnIndex.PROJECT_ID
import net.thechance.data.tasks.utils.TaskColumnIndex.TITLE
import net.thechance.logic.CsvSerializable
import net.thechance.logic.entities.State
import java.util.UUID



data class Project(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val description: String,
    val states: MutableList<State>,
    val tasks: MutableList<Task>,
    val createdBy: String //userId -> Admin
) : CsvSerializable {
    override fun toCsvFields(): List<String> = listOf(
        id,
        name,
        description,
        createdBy
    )

    companion object {
        fun fromCsv(fields: List<String>): Project {
            return Project(
                id = fields[0],
                name = fields[1],
                description = fields[2],
                states = mutableListOf(),
                tasks = mutableListOf(),
                createdBy = fields[3]
            )
        }
    }
}
//users
//projects + states
//tasks
//AuditLog