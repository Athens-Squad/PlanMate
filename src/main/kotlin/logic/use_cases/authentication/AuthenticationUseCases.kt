package logic.use_cases.authentication

data class AuthenticationUseCases(
    val loginUseCase: LoginUseCase,
    val registerAsAdminUseCase: RegisterAsAdminUseCase,
    val registerAsMateUseCase: RegisterAsMateUseCase
)
