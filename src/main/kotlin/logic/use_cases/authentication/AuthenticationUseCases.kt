package net.thechance.logic.use_cases.authentication

data class AuthenticationUseCases(
    val loginUseCase: LoginUseCase,
    val passwordHashing: PasswordHashing,
    val registerAsAdminUseCase: RegisterAsAdminUseCase,
    val registerAsMateUseCase: RegisterAsMateUseCase
)
