package net.thechance.logic.entities



sealed class UserType {
    data class MateUser(val adminId: String): UserType()
    data object AdminUser : UserType()
}