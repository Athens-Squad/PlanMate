package net.thechance.ui.handlers


import logic.entities.Project
import ui.io.ConsoleIO
import net.thechance.ui.options.project.ProjectOptions
import ui.featuresui.*

class ProjectOptionsHandler(
    private val consoleIO: ConsoleIO,
    private val projectsUi: ProjectsUi,
    private val statesUi: StatesUi,
    private val tasksUi: TasksUi,
    private val auditLogUi: AuditLogUi,
    private val project: Project
) {
    fun handle() {
        do {
            consoleIO.printer.printTitle("Select Option (1 to 7):")
            consoleIO.printer.printOptions(ProjectOptions.entries)

            val option = consoleIO.reader.readNumberFromUser()

            when (option) {
                ProjectOptions.CREATE_TASK.optionNumber -> createTask()
                ProjectOptions.EDIT.optionNumber -> projectsUi.editProject(project)
                ProjectOptions.MANAGE_STATES.optionNumber -> statesUi.manageStates(project.progressionStates)
                ProjectOptions.MANAGE_TASKS.optionNumber -> tasksUi.manageTasks(project.tasks, project.id, project.progressionStates)
                ProjectOptions.SHOW_HISTORY.optionNumber -> showHistory()
                ProjectOptions.DELETE.optionNumber -> deleteProject()
            }
        } while (option != ProjectOptions.BACK.optionNumber)
    }

    private fun createTask() {
        statesUi.getStates(project.id)
            .onSuccess { tasksUi.createTask(it, project.id) }
            .onFailure { consoleIO.printer.printError(it.message.toString()) }
    }

    private fun showHistory() {
        auditLogUi.getProjectHistory(project.id)
        auditLogUi.showHistoryOption()
    }

    private fun deleteProject() {
        projectsUi.deleteProject(project.id)
            .onSuccess { consoleIO.printer.printCorrectOutput("Project Deleted Successfully") }
            .onFailure { consoleIO.printer.printError(it.message.toString()) }
    }
}
