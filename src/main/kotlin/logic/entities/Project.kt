package logic.entities

import java.util.UUID



data class Project(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val description: String,
    val progressionStates: MutableList<ProgressionState>,
    val tasks: MutableList<Task>,
    val createdBy: String //userId -> Admin
)

//users
//projects + states
//tasks
//AuditLog