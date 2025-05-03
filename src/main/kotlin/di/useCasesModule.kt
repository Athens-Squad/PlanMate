package di

import logic.use_cases.audit_log.CreateAuditLogUseCase
import logic.use_cases.state.*
import logic.use_cases.task.*
import logic.use_cases.project.*
import logic.use_cases.authentication.*
import logic.use_cases.audit_log.*
import logic.use_cases.state.StatesUseCases
import logic.use_cases.project.GetAllProjectsByUsernameUseCase
import net.thechance.logic.use_cases.user.GetUserByUsernameUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val useCasesModule = module {
    singleOf(::LoginUseCase)
    singleOf(::RegisterAsMateUseCase)
    singleOf(::RegisterAsAdminUseCase)
    singleOf(::AuthenticationUseCases)

    singleOf(::GetUserByUsernameUseCase)

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

    singleOf(::CreateStateUseCase)
    singleOf(::DeleteStateUseCase)
    singleOf(::GetStateByIdUseCase)
    singleOf(::GetStatesByProjectIdUseCase)
    singleOf(::UpdateStateUseCase)
    singleOf(::StatesUseCases)

    singleOf(::CreateTaskUseCase)
    singleOf(::DeleteTaskUseCase)
    singleOf(::GetTaskByIdUseCase)
    singleOf(::GetTasksByProjectIdUseCase)
    singleOf(::UpdateTaskUseCase)
    singleOf(::TasksUseCases)

}