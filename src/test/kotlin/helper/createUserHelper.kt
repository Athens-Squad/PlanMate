package helper

import logic.entities.User
import net.thechance.logic.entities.UserType
import java.util.UUID

fun createUser(
    id: String = UUID.randomUUID().toString(),
    username: String = "Malak",
    password: String = "123Password",
    userType : UserType = UserType.MateUser(adminId = "12")
) = User(
    id = id,
    name = username,
    password = password,
    type = userType
)