@file:OptIn(ExperimentalUuidApi::class)

package net.thechance.ui.handlers


import kotlinx.coroutines.*
import logic.entities.Project
import net.thechance.ui.options.project.ProjectMateOptions
import ui.io.ConsoleIO
import net.thechance.ui.options.project.ProjectOptions
import net.thechance.ui.utils.TextStyle
import ui.featuresui.*
import kotlin.uuid.ExperimentalUuidApi

class ProjectOptionsHandler(
	private val consoleIO: ConsoleIO,
	private val projectsUi: ProjectsUi,
	private val progressionStateUi: ProgressionStateUi,
	private val tasksUi: TasksUi,
	private val auditLogUi: AuditLogUi,
) {
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        consoleIO.printer.printText("Unexpected error: ${throwable.message}",TextStyle.ERROR)
    }
    private val projectsScope: CoroutineScope =
        CoroutineScope(Dispatchers.IO + SupervisorJob() + exceptionHandler)

    private lateinit var project: Project


    suspend fun handleAdmin(project: Project) {
        this.project = project

        do {
            consoleIO.printer.printText("Select Option (1 to 7):",TextStyle.TITLE)
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
            consoleIO.printer.printText("Select Option (1 to 4):",TextStyle.TITLE)
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
            consoleIO.printer.printText("please create state first",TextStyle.ERROR)
            return
        }
        tasksUi.createTask(progressionStates, project.id)

        consoleIO.printer.printText("Task Created Successfully.",TextStyle.SUCCESS)
    }

    private fun showHistory() {
        try {
            projectsScope.launch {
                val history = auditLogUi.getProjectHistory(project.id)
                if(history.isEmpty()){
                    consoleIO.printer.printText("no history found",TextStyle.ERROR)
                    return@launch
                }
                history.forEach { log->
                    consoleIO.printer.printText(log.toString(), TextStyle.INFO)
                }
                auditLogUi.showHistoryOption()
            }

        } catch (exception : Exception){
            consoleIO.printer.printText(exception.message.toString(),TextStyle.ERROR)
        }
    }

    private fun deleteProject() {
        projectsUi.deleteProject(project.id)
        consoleIO.printer.printText("Project Deleted Successfully",TextStyle.SUCCESS)
    }
}
