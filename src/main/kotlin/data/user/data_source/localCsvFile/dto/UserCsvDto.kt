@file:OptIn(ExperimentalUuidApi::class)

package net.thechance.data.user.data_source.localCsvFile.dto

import data.user.utils.UserColumnIndex
import logic.entities.UserType
import net.thechance.data.utils.csv_file_handle.CsvSerializable
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

data class UserCsvDto(
	val id: Uuid = Uuid.random(),
	val name: String,
	val password: String,
	val type: UserType
) : CsvSerializable {
	override fun toCsvFields(): List<String> = listOf(
		id.toString(),
		name,
		password,
		type.toString()
	)

	companion object {
		fun fromCsv(fields: List<String>): UserCsvDto {
			return UserCsvDto(
				id = Uuid.parse(fields[UserColumnIndex.ID]),
				name = fields[UserColumnIndex.USERNAME],
				password = fields[UserColumnIndex.PASSWORD],
				type = if (fields[UserColumnIndex.USER_TYPE] == "AdminUser") {
					UserType.AdminUser
				} else {
					UserType.MateUser(fields[UserColumnIndex.USER_TYPE].substring(UserColumnIndex.USER_NAME_STARTING_INDEX, fields[UserColumnIndex.USER_TYPE].length - 1))
				}
			)
		}
	}

}