@file:OptIn(ExperimentalUuidApi::class)

package logic.entities

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

data class User(
	val id: Uuid = Uuid.random(),
	val name: String,
	val type: UserType
)