package di

import logic.repositories.*
import data.user.repository.UserRepositoryImpl
import net.thechance.data.aduit_log.repository.AuditLogRepositoryImpl
import data.authentication.repository.AuthRepositoryImpl
import data.projects.ProjectsRepositoryImpl
import data.progression_state.repository.ProgressionStateRepositoryImpl
import data.tasks.repository.TasksRepositoryImpl
import logic.repositories.AuthenticationRepository
import org.koin.dsl.module

val repositoriesModule = module {

    single<AuditRepository> { AuditLogRepositoryImpl(get()) }

    single<ProjectsRepository> { ProjectsRepositoryImpl(get()) }
    single<ProgressionStateRepository> { ProgressionStateRepositoryImpl(get()) }

    single<TasksRepository> { TasksRepositoryImpl(get()) }


    single<UserRepository> { UserRepositoryImpl(get()) }
    single<AuthenticationRepository> { AuthRepositoryImpl(get()) }

}