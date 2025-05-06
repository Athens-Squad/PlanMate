package ui.featuresui

import kotlinx.coroutines.*
import logic.entities.AuditLog
import logic.use_cases.audit_log.AuditLogUseCases
import net.thechance.ui.options.audit_log.AuditLogOptions
import ui.io.ConsoleIO

class AuditLogUi(
    private val consoleIO: ConsoleIO,
    private val auditLogUseCases: AuditLogUseCases
) {
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        consoleIO.printer.printError("Unexpected error: ${throwable.message}")
    }
    private val logScope: CoroutineScope =
        CoroutineScope(Dispatchers.IO + SupervisorJob() + exceptionHandler)


    suspend fun getTaskHistory(taskId: String): List<AuditLog> {
        consoleIO.printer.printTitle("Here is The History of Your Task")
        return auditLogUseCases.getAuditLogsByTaskIdUseCase.execute(taskId)
    }

    suspend fun getProjectHistory(projectId: String): List<AuditLog> {
        consoleIO.printer.printTitle("Here is The History of Your Project")
        return auditLogUseCases.getAuditLogsByProjectIdUseCase.execute(projectId)
    }

    private suspend fun clearLog() {
         auditLogUseCases.clearLogUseCase.execute()
    }


    fun showHistoryOption() {
        consoleIO.printer.printTitle("Select Option (1 , 2 )")
        consoleIO.printer.printOptions(AuditLogOptions.entries)
        val inputHistoryOption = consoleIO.reader.readNumberFromUser()

        when (inputHistoryOption) {
            AuditLogOptions.CLEAR_LOG.optionNumber ->{
                logScope.launch {
                    try {
                        clearLog()
                        consoleIO.printer.printCorrectOutput("History Deleted Successfully.")
                    } catch (exception: Exception) {
                        consoleIO.printer.printError("Error : ${exception.message}")
                    }
                }

            }
            AuditLogOptions.BACK.optionNumber ->{
                return
            }
        }

    }
}