package net.thechance.logic.use_cases.audit_log

import logic.use_cases.audit_log.CreateAuditLogUseCase

data class AuditLogUseCases(
    val clearLogUseCase: ClearLogUseCase,
    val createAuditLogUseCase: CreateAuditLogUseCase,
    val getAuditLogsByTaskIdUseCase: GetAuditLogsByTaskIdUseCase,
    val getAuditLogsByProjectIdUseCase: GetAuditLogsByProjectIdUseCase
)
