package net.thechance.ui.handlers

import logic.entities.Project
import ui.io.ConsoleIO


class ProjectSwimlaneUiPrinter(private val consoleIO: ConsoleIO) {

    fun printSwimlanes(project: Project) {
        val stateToTasksMap = project.progressionStates.associate { state ->
            val tasks = project.tasks
                .filter { it.currentProgressionState.id == state.id }
                .map { it.title }
            state.name to tasks
        }

        val maxTasks = stateToTasksMap.values.maxOfOrNull { it.size } ?: 0
        val normalizedColumns = stateToTasksMap.mapValues { (_, tasks) ->
            tasks + List(maxTasks - tasks.size) { "" }
        }

        val headers = normalizedColumns.keys.toList()
        val colWidth = 25
        val formatRow: (List<String>) -> String = { row ->
            row.joinToString(" | ") { it.padEnd(colWidth) }
        }

        consoleIO.printer.printPlainText(formatRow(headers))
        consoleIO.printer.printPlainText("-".repeat((colWidth + 3) * headers.size))

        for (i in 0 until maxTasks) {
            val row = headers.map { header -> normalizedColumns[header]?.get(i) ?: "" }
            consoleIO.printer.printPlainText(formatRow(row))
        }
    }
}
