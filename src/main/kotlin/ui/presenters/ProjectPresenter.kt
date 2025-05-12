@file:OptIn(ExperimentalUuidApi::class)

package net.thechance.ui.presenters

import logic.entities.ProgressionState
import logic.entities.Project
import logic.entities.Task
import net.thechance.ui.core.io.ConsoleIO
import net.thechance.ui.core.io.TextStyle
import net.thechance.ui.featuresui.AuditLogUi
import net.thechance.ui.featuresui.ProgressionStateUi
import net.thechance.ui.featuresui.ProjectsUi
import net.thechance.ui.featuresui.TasksUi
import net.thechance.ui.options.project.ProjectMateOptions
import net.thechance.ui.options.project.ProjectOptions
import kotlin.uuid.ExperimentalUuidApi

class ProjectPresenter(
    private val consoleIO: ConsoleIO,
    private val projectsUi: ProjectsUi,
    private val tasksUi: TasksUi,
    private val progressionStateUi: ProgressionStateUi,
    private val auditLogsUi: AuditLogUi
) {
    suspend fun showDetails(project: Project, isAdmin: Boolean) {
        val states = projectsUi.getProgressionStatesByProjectId(project.id)
        val tasks = projectsUi.getTasksByProjectId(project.id)

        showSwimlane(project, states, tasks)

        if (isAdmin) {
            handleAdmin(project)
        } else {
            handleMate(project)
        }
    }

    private fun showSwimlane(
        project: Project,
        progressionStates: List<ProgressionState>,
        tasks: List<Task>
    ) {
        consoleIO.printer.printText("Project: ${project.name}", TextStyle.TITLE)
        consoleIO.printer.printText("Description: ${project.description}", TextStyle.INFO)
        consoleIO.printer.printText("-----------------------Tasks-----------------------")

        val swimlanes = progressionStates.associateWith { state ->
            tasks.filter { it.currentProgressionState.id == state.id }
        }

        for (state in progressionStates) {
            consoleIO.printer.printText("== ${state.name} ==", TextStyle.TITLE)
            val stateTasks = swimlanes[state].orEmpty()
            if (stateTasks.isEmpty()) consoleIO.printer.printText("(No tasks)", TextStyle.INFO)
            else stateTasks.forEach {
                consoleIO.printer.printText(" -- ${it.title}: ${it.description}", TextStyle.INFO)
            }
            consoleIO.printer.printText("---------------------------------------------------")
        }
    }

    private suspend fun handleAdmin(project: Project) {

        do {
            consoleIO.printer.printText("Select Option (1 to 7):", TextStyle.TITLE)
            consoleIO.printer.printOptions(ProjectOptions.entries)

            val option = consoleIO.reader.readNumberFromUser()

            when (option) {
                ProjectOptions.CREATE_TASK.optionNumber -> tasksUi.createTask(
                    project.id,
                    projectsUi.getProgressionStatesByProjectId(
                        project.id
                    )
                )

                ProjectOptions.EDIT.optionNumber -> projectsUi.editProject(project)

                ProjectOptions.MANAGE_STATES.optionNumber -> progressionStateUi.manageStates(project.id)

                ProjectOptions.MANAGE_TASKS.optionNumber -> tasksUi.manageTasks(
                    projectsUi.getTasksByProjectId(project.id),
                    projectsUi.getProgressionStatesByProjectId(project.id)
                )

                ProjectOptions.SHOW_HISTORY.optionNumber -> auditLogsUi.showTaskHistory(project.id)

                ProjectOptions.DELETE.optionNumber -> projectsUi.deleteProject(project.id)
            }
        } while (option != ProjectOptions.BACK.optionNumber && option != ProjectOptions.DELETE.optionNumber)
    }

    private suspend fun handleMate(project: Project) {

        do {
            consoleIO.printer.printText("Select Option (1 to 4):", TextStyle.TITLE)
            consoleIO.printer.printOptions(ProjectMateOptions.entries)

            val option = consoleIO.reader.readNumberFromUser()

            when (option) {
                ProjectMateOptions.CREATE_TASK.optionNumber -> tasksUi.createTask(
                    project.id,
                    projectsUi.getProgressionStatesByProjectId(
                        project.id
                    )
                )

                ProjectMateOptions.MANAGE_TASKS.optionNumber -> tasksUi.manageTasks(
                    projectsUi.getTasksByProjectId(project.id),
                    projectsUi.getProgressionStatesByProjectId(project.id)
                )

                ProjectMateOptions.SHOW_HISTORY.optionNumber -> auditLogsUi.showProjectHistory(project.id)
            }
        } while (option != ProjectMateOptions.BACK.optionNumber)
    }

}
