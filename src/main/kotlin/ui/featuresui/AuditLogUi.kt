package ui.featuresui

import logic.entities.AuditLog
import logic.use_cases.audit_log.AuditLogUseCases
import ui.io.ConsoleIO

class AuditLogUi(
    private val consoleIO: ConsoleIO,
    private val auditLogUseCases: AuditLogUseCases
) {
    fun getTaskHistory(taskId: String): Result<List<AuditLog>> {
        consoleIO.printer.printTitle("Here is The History of Your Task")
        return auditLogUseCases.getAuditLogsByTaskIdUseCase.execute(taskId)
    }

    fun getProjectHistory(projectId: String): Result<List<AuditLog>> {
        consoleIO.printer.printTitle("Here is The History of Your Project")
        return auditLogUseCases.getAuditLogsByProjectIdUseCase.execute(projectId)
    }

    fun clearLog(): Result<Unit> {
        return auditLogUseCases.clearLogUseCase.execute()

    }
}