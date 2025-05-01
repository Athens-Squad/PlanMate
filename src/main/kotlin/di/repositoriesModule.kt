package net.thechance.di

import logic.repositories.AuditRepository
import logic.repositories.ProjectsRepository
import logic.repositories.TasksRepository
import net.thechance.data.aduit_log_csvfile.repository.AuditLogRepositoryImpl
import net.thechance.data.projects.ProjectsRepositoryImpl
import net.thechance.data.tasks.repository.TasksRepositoryImpl
import org.koin.dsl.module

val repositoriesModule = module {
    single<TasksRepository> { TasksRepositoryImpl(get()) }

    single<ProjectsRepository> { ProjectsRepositoryImpl(get()) }
    single<AuditRepository> {AuditLogRepositoryImpl(get()) }
}