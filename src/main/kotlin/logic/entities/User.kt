package logic.entities

import net.thechance.data.user.utils.UserColumnIndex.ID
import net.thechance.data.user.utils.UserColumnIndex.PASSWORD
import net.thechance.data.user.utils.UserColumnIndex.USERNAME
import net.thechance.data.user.utils.UserColumnIndex.USER_TYPE
import net.thechance.logic.CsvSerializable
import net.thechance.logic.entities.UserType
import java.util.*


data class User(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val password: String,
    val type: UserType
) : CsvSerializable {
    override fun toCsvFields(): List<String> = listOf(id, name, password, type.toString())

    companion object {
        fun fromCsv(fields: List<String>): User {
            return User(
                id = fields[ID],
                name = fields[USERNAME],
                password = fields[PASSWORD],
                type = if (fields[USER_TYPE] == "AdminUser") {
                    UserType.AdminUser
                } else {
                    UserType.MateUser(fields[USER_TYPE].substring(17, fields[USER_TYPE].length - 1))
                }
            )
        }
    }

}