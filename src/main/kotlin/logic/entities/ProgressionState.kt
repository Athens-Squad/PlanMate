package logic.entities


import data.states.utils.StateColumIndex.NAME
import data.states.utils.StateColumIndex.ID
import data.states.utils.StateColumIndex.PROJECT_ID
import logic.CsvSerializable
import java.util.*


data class ProgressionState( // rename to ProgressionState
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
        fun fromCsv(fields: List<String>): ProgressionState {
            return ProgressionState(
                id = fields[ID],
                name = fields[NAME],
                projectId = fields[PROJECT_ID]
            )
        }
    }
}
