package logic.entities

import net.thechance.logic.entities.UserType
import java.util.UUID


data class User(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val password: String,
    val type: UserType
)