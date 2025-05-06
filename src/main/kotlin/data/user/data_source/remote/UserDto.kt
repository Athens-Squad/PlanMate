package net.thechance.data.user.data_source.remote

import data.user.utils.UserColumnIndex.ID
import data.user.utils.UserColumnIndex.PASSWORD
import data.user.utils.UserColumnIndex.USERNAME
import data.user.utils.UserColumnIndex.USER_NAME_STARTING_INDEX
import data.user.utils.UserColumnIndex.USER_TYPE
import logic.CsvSerializable
import logic.entities.User
import logic.entities.UserType
import org.bson.codecs.pojo.annotations.BsonId
import java.util.*

data class UserDto(
    @BsonId val id: String = UUID.randomUUID().toString(),
    val name: String,
    val password: String,
    val type: UserType
) {
    fun toUser() = User(
        id = id, name = name, password = password,
        type = type
    )

    companion object {
        fun fromUser(user: User)=UserDto(
            id=user.id,
            name = user.name,
            password = user.password,
            type = user.type
        )
    }
}