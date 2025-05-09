package net.thechance.data.user.dto

import data.user.utils.UserColumnIndex.ID
import data.user.utils.UserColumnIndex.PASSWORD
import data.user.utils.UserColumnIndex.USERNAME
import data.user.utils.UserColumnIndex.USER_NAME_STARTING_INDEX
import data.user.utils.UserColumnIndex.USER_TYPE
import logic.entities.UserType
import net.thechance.data.utils.CsvSerializable
import java.util.*

data class UserCsvDto(
	val id: String = UUID.randomUUID().toString(),
	val name: String,
	val password: String,
	val type: UserType
) : CsvSerializable {
	override fun toCsvFields(): List<String> = listOf(id, name, password, type.toString())

	companion object {
		fun fromCsv(fields: List<String>): UserCsvDto {
			return UserCsvDto(
				id = fields[ID],
				name = fields[USERNAME],
				password = fields[PASSWORD],
				type = if (fields[USER_TYPE] == "AdminUser") {
					UserType.AdminUser
				} else {
					UserType.MateUser(fields[USER_TYPE].substring(USER_NAME_STARTING_INDEX, fields[USER_TYPE].length - 1))
				}
			)
		}
	}

}