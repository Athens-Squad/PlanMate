package logic.use_cases.audit_log

data class AuditLogUseCases(
    val clearLogUseCase: ClearLogUseCase,
    val createAuditLogUseCase: CreateAuditLogUseCase,
    val getAuditLogsByTaskIdUseCase: GetAuditLogsByTaskIdUseCase,
    val getAuditLogsByProjectIdUseCase: GetAuditLogsByProjectIdUseCase
)
