package logic.entities

import net.thechance.data.projects.utils.ProjectColumnIndex.CREATED_BY
import net.thechance.data.projects.utils.ProjectColumnIndex.DESCRIPTION
import net.thechance.data.projects.utils.ProjectColumnIndex.ID
import net.thechance.data.projects.utils.ProjectColumnIndex.NAME
import net.thechance.logic.CsvSerializable
import net.thechance.logic.entities.State
import java.util.*


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
                id = fields[ID],
                name = fields[NAME],
                description = fields[DESCRIPTION],
                states = mutableListOf(),
                tasks = mutableListOf(),
                createdBy = fields[CREATED_BY]
            )
        }
    }
}
//users
//projects + states
//tasks
//AuditLog