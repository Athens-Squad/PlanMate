package net.thechance.di

import net.thechance.data.authentication.utils.PasswordHashing
import net.thechance.logic.use_cases.authentication.*
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val useCasesModule = module {
    single { LoginUseCase(get(), get()) }
    single { RegisterAsMateUseCase(get(), get()) }
    single { RegisterAsAdminUseCase(get(), get()) }
    singleOf(::AuthenticationUseCases)
}