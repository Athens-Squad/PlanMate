package net.thechance.di

import net.thechance.data.user.repository.UserRepositoryImpl
import logic.repositories.TasksRepository
import logic.repositories.UserRepository
import net.thechance.data.authentication.repository.AuthRepositoryImpl
import net.thechance.data.tasks.repository.TasksRepositoryImpl
import net.thechance.logic.repositories.AuthenticationRepository
import org.koin.dsl.module

val repositoriesModule = module {
    single<TasksRepository> { TasksRepositoryImpl(get()) }

    single<UserRepository> { UserRepositoryImpl(get()) }

    single<AuthenticationRepository> { AuthRepositoryImpl(get()) }
}