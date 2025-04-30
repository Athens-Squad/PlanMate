package net.thechance.di

import logic.repositories.ProjectsRepository
import logic.repositories.TasksRepository
import net.thechance.data.projects.ProjectsRepositoryImpl
import net.thechance.data.tasks.repository.TasksRepositoryImpl
import org.koin.dsl.module
import kotlin.math.sin

val repositoriesModule = module {
    single<TasksRepository> { TasksRepositoryImpl(get()) }

    single<ProjectsRepository> { ProjectsRepositoryImpl(get()) }
}