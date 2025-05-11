package di

import logic.use_cases.audit_log.*
import logic.use_cases.authentication.AuthenticationUseCases
import logic.use_cases.authentication.LoginUseCase
import logic.use_cases.authentication.RegisterAsAdminUseCase
import logic.use_cases.authentication.RegisterAsMateUseCase
import logic.use_cases.progression_state.*
import logic.use_cases.project.*
import logic.use_cases.task.*
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val useCasesModule = module {
    singleOf(::LoginUseCase)
    singleOf(::RegisterAsMateUseCase)
    singleOf(::RegisterAsAdminUseCase)
    singleOf(::AuthenticationUseCases)

    singleOf(::ClearLogUseCase)
    singleOf(::GetAuditLogsByTaskIdUseCase)
    singleOf(::GetAuditLogsByProjectIdUseCase)
    singleOf(::CreateAuditLogUseCase)
    singleOf(::AuditLogUseCases)

    singleOf(::CreateProjectUseCase)
    singleOf(::DeleteProjectUseCase)
    singleOf(::UpdateProjectUseCase)
    singleOf(::GetProjectByIdUseCase)
    singleOf(::GetAllProjectsByUsernameUseCase)
    singleOf(::ProjectUseCases)

    singleOf(::CreateProgressionStateUseCase)
    singleOf(::DeleteProgressionStateUseCase)
    singleOf(::GetProgressionStateByIdUseCase)
    singleOf(::GetProgressionStatesByProjectIdUseCase)
    singleOf(::UpdateProgressionStateUseCase)
    singleOf(::ProgressionStatesUseCases)

    singleOf(::CreateTaskUseCase)
    singleOf(::DeleteTaskUseCase)
    singleOf(::GetTaskByIdUseCase)
    singleOf(::GetTasksByProjectIdUseCase)
    singleOf(::UpdateTaskUseCase)
    singleOf(::TasksUseCases)

}