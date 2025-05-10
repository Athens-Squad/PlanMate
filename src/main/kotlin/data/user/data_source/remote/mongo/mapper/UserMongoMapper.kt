@file:OptIn(ExperimentalUuidApi::class)

package data.user.data_source.remote.mongo.mapper

import logic.entities.User
import logic.entities.UserType
import net.thechance.data.user.data_source.remote.mongo.dto.UserDto
import kotlin.uuid.ExperimentalUuidApi

fun UserDto.toUser() = User(
	id = id,
	name = name,
	type = when (userType) {
		"Mate" -> UserType.MateUser(adminName ?: throw Exception("Couldn't find the admin"))
		"Admin" -> UserType.AdminUser
		else -> throw Exception("Invalid User Type")
	}
)


fun User.toUserDto(password: String): UserDto {
	return when (type) {
		is UserType.AdminUser -> UserDto(
			id = id,
			name = name,
			password = password,
			userType = "Admin"
		)

		is UserType.MateUser -> UserDto(
			id = id,
			name = name,
			password = password,
			userType = "Mate",
			adminName = type.adminName
		)
	}
}