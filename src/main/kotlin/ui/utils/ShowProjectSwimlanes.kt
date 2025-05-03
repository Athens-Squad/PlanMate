package net.thechance.ui.utils

import logic.entities.Project
import ui.io.ConsoleIO

class ShowProjectSwimlanes(private val consoleIO: ConsoleIO) {

    operator fun invoke(project: Project) {
        consoleIO.printer.printTitle("Project: ${project.name}")
        consoleIO.printer.printPlainText("Description: ${project.description}")
        consoleIO.printer.printDivider()

        val swimlanes = project.progressionStates.associateWith { state ->
            project.tasks.filter { it.currentProgressionState.id == state.id }
        }

        project.progressionStates.forEach { state ->
            consoleIO.printer.printTitle("== ${state.name}")
            val tasksInState = swimlanes[state].orEmpty()
            if (tasksInState.isEmpty()) {
                consoleIO.printer.printPlainText("  (No tasks)")
            } else {
                tasksInState.forEach { task ->
                    consoleIO.printer.printOption(" -- ${task.title}: ", false)

                    consoleIO.printer.printInfoLine(task.description)

                }
            }
            consoleIO.printer.printDivider()
        }
    }

}