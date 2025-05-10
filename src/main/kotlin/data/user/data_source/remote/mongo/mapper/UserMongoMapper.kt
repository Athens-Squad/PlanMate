package data.user.data_source.remote.mongo.mapper

import logic.entities.User
import logic.entities.UserType
import net.thechance.data.user.data_source.remote.mongo.dto.UserDto

fun UserDto.toUser() = User(
	id = id, name = name, password = password,
	type = when (userType) {
		"Mate" -> UserType.MateUser(adminName ?: throw Exception("Couldn't find the admin"))
		"Admin" -> UserType.AdminUser
		else -> throw Exception("Invalid User Type")
	}
)


fun User.toUserDto(): UserDto {
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