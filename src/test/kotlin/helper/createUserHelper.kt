@file:OptIn(ExperimentalUuidApi::class)

package helper

import logic.entities.User
import logic.entities.UserType
import java.util.UUID
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

fun createUser(
    id: Uuid = Uuid.random(),
    username: String = "Malak",
    userType : UserType = UserType.MateUser(adminName = "12")
) = User(
    id = id,
    name = username,
    type = userType
)