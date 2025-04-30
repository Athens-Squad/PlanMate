package net.thechance.di

import logic.repositories.TasksRepository
import net.thechance.data.tasks.repository.TasksRepositoryImpl
import org.koin.dsl.module

val repositoriesModule = module {
    single<TasksRepository> { TasksRepositoryImpl(get()) }
}