@file:OptIn(ExperimentalUuidApi::class)

package net.thechance.ui.featuresui

import logic.entities.AuditLog
import logic.use_cases.audit_log.AuditLogUseCases
import net.thechance.ui.core.io.ConsoleIO
import net.thechance.ui.core.io.TextStyle
import net.thechance.ui.options.audit_log.AuditLogOptions
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class AuditLogUi(
    private val consoleIO: ConsoleIO,
    private val auditLogUseCases: AuditLogUseCases
) {

    suspend fun showTaskHistory(taskId: Uuid) {
        consoleIO.printer.printText(
            "Here is The History of Your Task",
            TextStyle.TITLE
        )

        val taskHistory = auditLogUseCases.getAuditLogsByTaskIdUseCase.execute(taskId)
        if (taskHistory.isEmpty()) {
            consoleIO.printer.printText("No history found", TextStyle.ERROR)
            return
        }
        taskHistory.forEach { log ->
            printLog(log)
        }
    }

    suspend fun showProjectHistory(projectId: Uuid) {
        consoleIO.printer.printText(
            "Here is The History of Your Project",
            TextStyle.TITLE
        )

        try {
            val projectHistory = auditLogUseCases.getAuditLogsByProjectIdUseCase.execute(projectId)
            if (projectHistory.isEmpty()) {
                consoleIO.printer.printText("No history found", TextStyle.ERROR)
                return
            }

            projectHistory.forEach { log ->
                printLog(log)
            }

            showHistoryOption()
        } catch (exception: Exception) {
            consoleIO.printer.printText(exception.message.toString(), TextStyle.ERROR)
        }
    }

    private suspend fun showHistoryOption() {
        consoleIO.printer.printText(
            "Select Option (1 , 2 )",
            TextStyle.TITLE
        )
        consoleIO.printer.printOptions(AuditLogOptions.entries)

        val inputHistoryOption = consoleIO.reader.readNumberFromUser()
        when (inputHistoryOption) {
            AuditLogOptions.CLEAR_LOG.optionNumber -> clearHistory()

            AuditLogOptions.BACK.optionNumber -> {
                return
            }
        }

    }

    private suspend fun clearHistory() {
        try {
            clearLog()
            consoleIO.printer.printText(
                "History Deleted Successfully.",
                TextStyle.SUCCESS
            )
        } catch (exception: Exception) {
            consoleIO.printer.printText(
                "Error: ${exception.message}",
                TextStyle.ERROR
            )
        }

    }

    private suspend fun clearLog() {
        auditLogUseCases.clearLogUseCase.execute()
    }

    private fun printLog(log: AuditLog) {
        consoleIO.printer.printText(
            "User: ${log.userName}",
            TextStyle.INFO
        )

        consoleIO.printer.printText(
            "Changed ${log.entityType.name} : ${log.entityId}",
            TextStyle.INFO
        )

        consoleIO.printer.printText(
            "Description : ${log.description}",
            TextStyle.INFO
        )

        consoleIO.printer.printText(
            "At: ${log.createdAt}",
            TextStyle.INFO
        )

        consoleIO.printer.printText("--------------------------------------", TextStyle.INFO)
    }
}