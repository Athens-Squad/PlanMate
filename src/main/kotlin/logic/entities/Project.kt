package logic.entities

import java.util.UUID



data class Project(
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
        fun fromCsv(fields: List<String>): Project {
            return Project(
                id = fields[ID],
                name = fields[NAME],
                description = fields[DESCRIPTION],
                createdBy = fields[CREATED_BY]
            )
        }
    }
}