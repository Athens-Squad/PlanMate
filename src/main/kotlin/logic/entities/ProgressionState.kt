package logic.entities


import java.util.UUID


data class ProgressionState( // rename to ProgressionState
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val projectId: String
)
