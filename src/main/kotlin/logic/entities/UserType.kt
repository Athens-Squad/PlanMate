package logic.entities



sealed class UserType {
    data class MateUser(val adminName: String): UserType()
    data object AdminUser : UserType()
}