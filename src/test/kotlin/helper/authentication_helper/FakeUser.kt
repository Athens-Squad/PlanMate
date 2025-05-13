@file:OptIn(ExperimentalUuidApi::class)
package helper.authentication_helper

import logic.entities.User
import logic.entities.UserType
import kotlin.uuid.ExperimentalUuidApi

object FakeUser {
    val createUser = User(
        name = "mohamed ragab",
        type = UserType.AdminUser
    )
}