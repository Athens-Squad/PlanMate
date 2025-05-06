package net.thechance.ui.handlers


import logic.entities.Project
import net.thechance.ui.options.project.ProjectMateOptions
import ui.io.ConsoleIO
import net.thechance.ui.options.project.ProjectOptions
import ui.featuresui.*

class ProjectOptionsHandler(
	private val consoleIO: ConsoleIO,
	private val projectsUi: ProjectsUi,
	private val progressionStateUi: ProgressionStateUi,
	private val tasksUi: TasksUi,
	private val auditLogUi: AuditLogUi,
) {
    private lateinit var project: Project


    suspend fun handleAdmin(project: Project) {
        this.project = project

        do {
            consoleIO.printer.printTitle("Select Option (1 to 7):")
            consoleIO.printer.printOptions(ProjectOptions.entries)

            val option = consoleIO.reader.readNumberFromUser()

            when (option) {
                ProjectOptions.CREATE_TASK.optionNumber -> createTask()
                ProjectOptions.EDIT.optionNumber -> projectsUi.editProject(project)
                ProjectOptions.MANAGE_STATES.optionNumber -> progressionStateUi.manageStates( project.id)
                ProjectOptions.MANAGE_TASKS.optionNumber -> tasksUi.manageTasks(project.tasks, project.id, project.progressionStates)
                ProjectOptions.SHOW_HISTORY.optionNumber -> showHistory()
                ProjectOptions.DELETE.optionNumber -> deleteProject()
            }
        } while (option != ProjectOptions.BACK.optionNumber && option != ProjectOptions.DELETE.optionNumber)
    }

    suspend fun handleMate(project: Project) {
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

    private suspend fun createTask() {
        val progressionStates = progressionStateUi.getProgressionStatesByProjectId(project.id)
        if(progressionStates.isEmpty()){
            consoleIO.printer.printError("please create state first")
            return
        }
        tasksUi.createTask(progressionStates, project.id)

        consoleIO.printer.printCorrectOutput("Task Created Successfully.")
    }

    private suspend fun showHistory() {
        try {
            val history = auditLogUi.getProjectHistory(project.id)
            if(history.isEmpty()){
                consoleIO.printer.printError("no history found")
                return
            }
            history.forEach { log->
                consoleIO.printer.printInfoLine(log.toString())
            }
            auditLogUi.showHistoryOption()

        } catch (exception : Exception){
            consoleIO.printer.printError(exception.message.toString())
        }
    }

    private fun deleteProject() {
        projectsUi.deleteProject(project.id)
        consoleIO.printer.printCorrectOutput("Project Deleted Successfully")
    }
}
