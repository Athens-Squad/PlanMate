package net.thechance.data.user.data_source.localCsvFile.mapper

import logic.entities.User
import net.thechance.data.user.data_source.localCsvFile.dto.UserCsvDto

fun UserCsvDto.toUser() = User(
	id = id,
	name = name,
	password = password,
	type = type
)


fun User.toUserCsvDto() = UserCsvDto(
	id = id,
	name = name,
	password = password,
	type = type
)