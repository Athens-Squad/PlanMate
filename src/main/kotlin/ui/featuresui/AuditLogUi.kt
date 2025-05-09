package ui.featuresui

import kotlinx.coroutines.*
import logic.entities.AuditLog
import logic.use_cases.audit_log.AuditLogUseCases
import net.thechance.ui.options.audit_log.AuditLogOptions
import net.thechance.ui.utils.TextStyle
import ui.io.ConsoleIO

class AuditLogUi(
    private val consoleIO: ConsoleIO,
    private val auditLogUseCases: AuditLogUseCases
) {
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        consoleIO.printer.printText("Unexpected error: ${throwable.message}",TextStyle.ERROR)
    }
    private val logScope: CoroutineScope =
        CoroutineScope(Dispatchers.IO + SupervisorJob() + exceptionHandler)


    suspend fun getTaskHistory(taskId: String): List<AuditLog> {
        consoleIO.printer.printText("Here is The History of Your Task",TextStyle.TITLE)
        return auditLogUseCases.getAuditLogsByTaskIdUseCase.execute(taskId)
    }

    suspend fun getProjectHistory(projectId: String): List<AuditLog> {
        consoleIO.printer.printText("Here is The History of Your Project",TextStyle.TITLE)
        return auditLogUseCases.getAuditLogsByProjectIdUseCase.execute(projectId)
    }

    private suspend fun clearLog() {
         auditLogUseCases.clearLogUseCase.execute()
    }


    fun showHistoryOption() {
        consoleIO.printer.printText("Select Option (1 , 2 )",TextStyle.TITLE)
        consoleIO.printer.printOptions(AuditLogOptions.entries)
        val inputHistoryOption = consoleIO.reader.readNumberFromUser()

        when (inputHistoryOption) {
            AuditLogOptions.CLEAR_LOG.optionNumber ->{
                logScope.launch {
                    try {
                        clearLog()
                        consoleIO.printer.printText("History Deleted Successfully.",TextStyle.SUCCESS)
                    } catch (exception: Exception) {
                        consoleIO.printer.printText("Error : ${exception.message}",TextStyle.ERROR)
                    }
                }

            }
            AuditLogOptions.BACK.optionNumber ->{
                return
            }
        }

    }
}