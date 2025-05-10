package logic.entities

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid


@OptIn(ExperimentalUuidApi::class)
data class Project(
    val id: Uuid = Uuid.random(),
    val name: String,
    val description: String,
    val progressionStates: MutableList<ProgressionState> = mutableListOf(),
    val tasks: MutableList<Task> = mutableListOf(),
    val createdByUserName: String
)