package logic.entities

import java.util.*

data class ProgressionState(
	val id: String = UUID.randomUUID().toString(),
	val name: String,
	val projectId: String,
)