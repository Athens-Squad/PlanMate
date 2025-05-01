package net.thechance.di

import net.thechance.logic.use_cases.authentication.*
import org.koin.dsl.module

val useCasesModule = module {
    single { LoginUseCase(get(),get()) }
    single { PasswordHashing() }
    single { RegisterAsMateUseCase(get()) }
    single { RegisterAsAdminUseCase(get()) }

}