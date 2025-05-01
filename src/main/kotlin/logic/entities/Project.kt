package logic.entities

import logic.entities.State
import java.util.UUID



data class Project(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val description: String,
    val states: MutableList<State>,
    val tasks: MutableList<Task>,
    val createdBy: String //userId -> Admin
)

//users
//projects + states
//tasks
//AuditLog