package helper

import logic.entities.User
import net.thechance.logic.entities.UserType
import java.util.UUID

fun createUser(
    id: String = "1",
    username: String = "Malak",
    password: String = "123Password",
    userType : UserType = UserType.MateUser(adminId = "12")
) = User(
    id = id,
    name = username,
    password = password,
    type = userType
)