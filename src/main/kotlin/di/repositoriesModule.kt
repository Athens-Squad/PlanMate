package net.thechance.di

import logic.repositories.AuditRepository
import logic.repositories.StatesRepository
import net.thechance.data.user.repository.UserRepositoryImpl
import logic.repositories.TasksRepository
import logic.repositories.UserRepository
import net.thechance.data.aduit_log_csvfile.repository.AuditLogRepositoryImpl
import net.thechance.data.authentication.repository.AuthRepositoryImpl
import net.thechance.data.states.repository.StatesRepositoryImpl
import net.thechance.data.tasks.repository.TasksRepositoryImpl
import net.thechance.logic.repositories.AuthenticationRepository
import org.koin.dsl.module

val repositoriesModule = module {
    single<TasksRepository> { TasksRepositoryImpl(get()) }

    single<StatesRepository> { StatesRepositoryImpl(get())}

    single<UserRepository> { UserRepositoryImpl(get()) }

    single<AuthenticationRepository> { AuthRepositoryImpl(get()) }
    single<AuditRepository> {AuditLogRepositoryImpl(get()) }
}