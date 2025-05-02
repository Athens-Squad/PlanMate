package ui.featuresui

import logic.entities.AuditLog
import logic.use_cases.audit_log.AuditLogUseCases
import net.thechance.ui.options.audit_log.AuditLogOptions
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


    fun showHistoryOption() { //TODO() call from task history
        consoleIO.printer.printTitle("Select Option (1)")
        consoleIO.printer.printOptions(AuditLogOptions.entries)
        val inputHistoryOption = consoleIO.reader.readNumberFromUser()

        when (inputHistoryOption) {
            AuditLogOptions.CLEAR_LOG.optionNumber ->{
                clearLog()
                    .onSuccess {
                        consoleIO.printer.printCorrectOutput("History Deleted Successfully.")
                    }
                    .onFailure {
                        consoleIO.printer.printError(it.message.toString())
                    }
            }
        }

    }
}