package logic.entities

import java.util.*


data class Project(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val description: String,
    val progressionStates: MutableList<ProgressionState> = mutableListOf(),
    val tasks: MutableList<Task> = mutableListOf(),
    val createdBy: String
)