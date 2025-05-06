package net.thechance.ui.handlers


import logic.entities.Project
import net.thechance.ui.options.project.ProjectMateOptions
import ui.io.ConsoleIO
import net.thechance.ui.options.project.ProjectOptions
import ui.featuresui.*

class ProjectOptionsHandler(
    private val consoleIO: ConsoleIO,
    private val projectsUi: ProjectsUi,
    private val statesUi: StatesUi,
    private val tasksUi: TasksUi,
    private val auditLogUi: AuditLogUi,
) {
    private lateinit var project: Project
    fun handleAdmin(project: Project) {
        this.project = project

        do {
            consoleIO.printer.printTitle("Select Option (1 to 7):")
            consoleIO.printer.printOptions(ProjectOptions.entries)

            val option = consoleIO.reader.readNumberFromUser()

            when (option) {
                ProjectOptions.CREATE_TASK.optionNumber -> createTask()
                ProjectOptions.EDIT.optionNumber -> projectsUi.editProject(project)
                ProjectOptions.MANAGE_STATES.optionNumber -> statesUi.manageStates(project.id)
                ProjectOptions.MANAGE_TASKS.optionNumber -> tasksUi.manageTasks(
                    project.tasks,
                    project.id,
                    project.progressionStates
                )

                ProjectOptions.SHOW_HISTORY.optionNumber -> showHistory()
                ProjectOptions.DELETE.optionNumber -> deleteProject()
            }
        } while (option != ProjectOptions.BACK.optionNumber && option != ProjectOptions.DELETE.optionNumber)
    }

    fun handleMate(project: Project) {
        this.project = project

        do {
            consoleIO.printer.printTitle("Select Option (1 to 4):")
            consoleIO.printer.printOptions(ProjectMateOptions.entries)

            val option = consoleIO.reader.readNumberFromUser()

            when (option) {
                ProjectMateOptions.CREATE_TASK.optionNumber -> createTask()
                ProjectMateOptions.MANAGE_TASKS.optionNumber -> tasksUi.manageTasks(
                    project.tasks,
                    project.id,
                    project.progressionStates
                )

                ProjectMateOptions.SHOW_HISTORY.optionNumber -> showHistory()
            }
        } while (option != ProjectMateOptions.BACK.optionNumber)
    }

    private fun createTask() {
        try {
            statesUi.getStates(project.id)
                .onSuccess { states ->
                    if (states.isEmpty()) {
                        consoleIO.printer.printError("please create state first")
                        return
                    }
                    tasksUi.createTask(states, project.id)
                    consoleIO.printer.printCorrectOutput("Task Created Successfully.")
                }

        } catch (exception: Exception) {
            consoleIO.printer.printError(exception.message.toString())
        }
    }

    private fun showHistory() {
        auditLogUi.getProjectHistory(project.id).onSuccess { history ->
            if (history.isEmpty()) {
                consoleIO.printer.printError("no history found")
                return
            }
            history.forEach { log ->
                consoleIO.printer.printInfoLine(log.toString())
            }
        }
            .onFailure {
                consoleIO.printer.printError(it.message.toString())
                return
            }
        auditLogUi.showHistoryOption()
    }

    private fun deleteProject() {
        projectsUi.deleteProject(project.id)
            .onSuccess { consoleIO.printer.printCorrectOutput("Project Deleted Successfully") }
            .onFailure { consoleIO.printer.printError(it.message.toString()) }
    }
}
