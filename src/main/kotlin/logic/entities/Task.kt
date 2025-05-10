@file:OptIn(ExperimentalUuidApi::class)

package logic.entities

import java.util.*
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid


data class Task(
    val id: Uuid = Uuid.random(),
    val title: String,
    val description: String,
    val currentProgressionState: ProgressionState,
    val projectId: Uuid
)