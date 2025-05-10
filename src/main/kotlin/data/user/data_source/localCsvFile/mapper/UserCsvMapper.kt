@file:OptIn(ExperimentalUuidApi::class)

package net.thechance.data.user.data_source.localCsvFile.mapper

import logic.entities.User
import net.thechance.data.user.data_source.localCsvFile.dto.UserCsvDto
import kotlin.uuid.ExperimentalUuidApi

fun UserCsvDto.toUser() = User(
	id = id,
	name = name,
	type = type
)


fun User.toUserCsvDto(password: String) = UserCsvDto(
	id = id,
	name = name,
	password = password,
	type = type
)