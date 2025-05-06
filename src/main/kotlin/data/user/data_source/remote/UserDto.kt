package net.thechance.data.user.data_source.remote


import logic.entities.User
import logic.entities.UserType
import org.bson.codecs.pojo.annotations.BsonId
import java.util.*

data class UserDto(
    @BsonId val id: String = UUID.randomUUID().toString(),
    val name: String,
    val password: String,
    val userType: String,
    val adminName: String? = null
) {
    fun toUser() = User(
        id = id, name = name, password = password,
        type = when(userType) {
            "Mate" -> UserType.MateUser(adminName ?: throw Exception("Couldn't find the admin"))
            "Admin" -> UserType.AdminUser
            else -> throw Exception("Invalid User Type")
        }
    )

    companion object {

        fun fromUser(user: User): UserDto {
            return when(user.type) {
                is UserType.AdminUser -> UserDto(
                    id = user.id,
                    name = user.name,
                    password = user.password,
                    userType = "Admin"
                )

                is UserType.MateUser -> UserDto(
                    id = user.id,
                    name = user.name,
                    password = user.password,
                    userType = "Mate",
                    adminName = user.type.adminName
                )
            }
        }
    }
}