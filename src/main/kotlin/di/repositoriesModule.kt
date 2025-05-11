package di

import data.authentication.repository.AuthRepositoryImpl
import data.progression_state.repository.ProgressionStateRepositoryImpl
import data.projects.ProjectsRepositoryImpl
import data.tasks.repository.TasksRepositoryImpl
import data.user.repository.UserRepositoryImpl
import logic.repositories.*
import net.thechance.data.aduit_log.repository.AuditLogRepositoryImpl
import org.koin.dsl.module

val repositoriesModule = module {

    single<AuditRepository> { AuditLogRepositoryImpl(get()) }

    single<ProjectsRepository> { ProjectsRepositoryImpl(get()) }
    single<ProgressionStateRepository> { ProgressionStateRepositoryImpl(get()) }

    single<TasksRepository> { TasksRepositoryImpl(get()) }


    single<UserRepository> { UserRepositoryImpl(get()) }
    single<AuthenticationRepository> { AuthRepositoryImpl(get()) }

}