package ui.featuresui

import logic.entities.AuditLog
import logic.use_cases.audit_log.AuditLogUseCases
import ui.io.ConsoleIO

class AuditLogUi(
    private val consoleIO: ConsoleIO,
    private val auditLogUseCases: AuditLogUseCases
) {
    fun getTaskHistory(taskId: String): Result<List<AuditLog>> {
        return Result.success(listOf())
    }

    fun getProjectHistory(projectId: String): Result<List<AuditLog>> {

        return Result.success(listOf())
    }

    fun clearLog(): Result<Unit> {
return Result.success(Unit)
    }
}