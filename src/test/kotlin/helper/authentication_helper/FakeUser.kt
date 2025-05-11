package helper.authentication_helper

import logic.entities.User
import logic.entities.UserType

object FakeUser {
    val createUser = User(
        name = "mohamed ragab",
        password =  "123Password",
        type = UserType.AdminUser
    )
}