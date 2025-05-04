package net.thechance.ui.options


enum class AuthenticationOptions(
    override val optionNumber: Int,
    override val optionTitle: String
): Option {
    LOGIN(
        1,
        "Login"
    ),
    REGISTER_AS_ADMIN(
        2,
        "Register as Admin"
    )
}