package logic.entities

import java.util.*


data class User(
	val id: String = UUID.randomUUID().toString(),
	val name: String,
	val password: String,
	val type: UserType
)