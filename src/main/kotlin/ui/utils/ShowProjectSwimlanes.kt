@file:OptIn(ExperimentalUuidApi::class)

package net.thechance.ui.utils

import logic.entities.ProgressionState
import logic.entities.Project
import logic.entities.Task
import ui.io.ConsoleIO
import kotlin.uuid.ExperimentalUuidApi

class ShowProjectSwimlanes(private val consoleIO: ConsoleIO) {

    operator fun invoke(
        project: Project,
        progressionStates: List<ProgressionState>,
        tasks: List<Task>
    ) {
        consoleIO.printer.printText("Project: ${project.name}",TextStyle.TITLE)
        consoleIO.printer.printText("Description: ${project.description}")
        consoleIO.printer.printDivider()

        val swimlanes = progressionStates.associateWith { state ->
            tasks.filter { it.currentProgressionState.id == state.id }
        }

        progressionStates.forEach { state ->
            consoleIO.printer.printText("== ${state.name}",TextStyle.TITLE)
            val tasksInState = swimlanes[state].orEmpty()
            if (tasksInState.isEmpty()) {
                consoleIO.printer.printText("  (No tasks)")
            } else {
                tasksInState.forEach { task ->
                    consoleIO.printer.printText(" -- ${task.title}: ", TextStyle.OPTION,false)

                    consoleIO.printer.printText(task.description,TextStyle.INFO)

                }
            }
            consoleIO.printer.printDivider()
        }
    }

}