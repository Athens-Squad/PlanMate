@file:OptIn(ExperimentalUuidApi::class)

package logic.entities

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid


data class ProgressionState(
	val id: Uuid = Uuid.random(),
	val name: String,
	val projectId: Uuid,
)