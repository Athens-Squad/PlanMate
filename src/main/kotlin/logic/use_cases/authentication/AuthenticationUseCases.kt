package net.thechance.logic.use_cases.authentication

data class AuthenticationUseCases(
    val loginUseCase: LoginUseCase,
    val hashPasswordUseCase: HashPasswordUseCase,
    val registerAsAdminUseCase: RegisterAsAdminUseCase,
    val registerAsMateUseCase: RegisterAsMateUseCase
)
