package logic.entities

import java.util.*


data class Task(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val description: String,
    val currentProgressionState: ProgressionState,
    val projectId: String
)