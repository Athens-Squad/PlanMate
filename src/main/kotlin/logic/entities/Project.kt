package logic.entities

import java.util.UUID



data class Project(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val description: String,
    val progressionStates: MutableList<ProgressionState> = mutableListOf(),
    val tasks: MutableList<Task> = mutableListOf(),
    val createdBy: String //userName -> Admin
)

//users
//projects + states
//tasks
//AuditLog