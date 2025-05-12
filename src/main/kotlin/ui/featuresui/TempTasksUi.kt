@file:OptIn(ExperimentalUuidApi::class)

package net.thechance.ui.featuresui


import kotlinx.coroutines.*
import logic.entities.ProgressionState
import logic.entities.Task
import logic.use_cases.progression_state.GetProgressionStatesByProjectIdUseCase
import logic.use_cases.task.TasksUseCases
import net.thechance.data.authentication.UserSession
import net.thechance.ui.core.io.ConsoleIO
import net.thechance.ui.core.io.TextStyle
import net.thechance.ui.options.tasks.EditTaskOptions
import net.thechance.ui.options.tasks.TaskOptions
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid


class TempTasksUi(
    private val consoleIO: ConsoleIO,
    private val tasksUseCases: TasksUseCases,
    private val getProgressionStatesByProjectIdUseCase: GetProgressionStatesByProjectIdUseCase,
    private val auditLogUi: AuditLogUi,
    private val session: UserSession
) {
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        printError(throwable.message.toString())
    }

    private val tasksCoroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob() + exceptionHandler)

    suspend fun manageTasks(tasks: List<Task>, projectId: Uuid) {
        tasksCoroutineScope.launch {
            try {
                do {
                    printTitle("Select A Task:")
                    val inputTaskName = consoleIO.reader.readStringFromUser()
                    if (inputTaskName == "0") return@launch

                    val task = getTaskByName(inputTaskName, tasks)
                    task.showTaskDetails()
                    handleTaskOptions(task, projectId)
                } while (true)
            } catch (exception: Exception) {
                printError(exception.message.toString())
            }
        }.join()
    }

    suspend fun createTask(projectId: Uuid) {
        printTitle("Create Task")
        val taskName = receiveStringInput("Enter Task Name:")
        val taskDescription = receiveStringInput("Enter Task Description:")
        val state = selectProgressionState(projectId)

        tasksCoroutineScope.launch {
            tasksUseCases.createTaskUseCase.execute(
                Task(
                    title = taskName,
                    description = taskDescription,
                    currentProgressionState = state,
                    projectId = projectId
                ),
                userName = session.currentUser.name
            )
        }
    }

    private suspend fun handleTaskOptions(task: Task, projectId: Uuid) {
        do {
            printTitle("Select Option (1 to 4):")
            consoleIO.printer.printOptions(TaskOptions.entries)

            when (val option = consoleIO.reader.readNumberFromUser()) {
                TaskOptions.EDIT.optionNumber -> editTask(task, projectId)
                TaskOptions.SHOW_HISTORY.optionNumber -> showTaskHistory(task)
                TaskOptions.DELETE.optionNumber -> deleteTaskWithConfirmation(task)
                TaskOptions.BACK.optionNumber -> return
            }
        } while (option != TaskOptions.BACK.optionNumber)
    }

    private suspend fun showTaskHistory(task: Task) {
        tasksCoroutineScope.launch {
            try {
                auditLogUi.showTaskHistory(task.id)
                auditLogUi.showHistoryOption()
            } catch (exception: Exception) {
                printError("Error: ${exception.message}")
            }
        }.join()
    }

    private fun editTask(task: Task, projectId: Uuid) {
        printTitle("Edit Task")
        consoleIO.printer.printOptions(EditTaskOptions.entries)

        when (consoleIO.reader.readNumberFromUser()) {
            EditTaskOptions.NAME.optionNumber -> updateTaskName(task)
            EditTaskOptions.DESCRIPTION.optionNumber -> updateTaskDescription(task)
            EditTaskOptions.PROGRESSION_STATE.optionNumber -> updateTaskState(task, projectId)
        }
    }

    private fun updateTaskName(task: Task) {
        val newName = receiveStringInput("Enter New Task Name:")
        updateTask(task.copy(title = newName))
    }

    private fun updateTaskDescription(task: Task) {
        val newDescription = receiveStringInput("Enter New Task Description:")
        updateTask(task.copy(description = newDescription))
    }

    private fun updateTaskState(task: Task, projectId: Uuid) {
        val state = selectProgressionState(projectId)
        updateTask(task.copy(currentProgressionState = state))
    }

    private fun updateTask(updatedTask: Task) {
        tasksCoroutineScope.launch {
            tasksUseCases.updateTaskUseCase.execute(
                updatedTask,
                userName = session.currentUser.name
            )
        }
    }

    private fun deleteTaskWithConfirmation(task: Task) {
        tasksCoroutineScope.launch {
            tasksUseCases.deleteTaskUseCase.execute(
                taskId = task.id,
                userName = session.currentUser.name
            )
            printSuccess("Task Deleted Successfully")
        }
    }

    private suspend fun selectProgressionState(projectId: Uuid): ProgressionState {
        val states = getProgressionStatesByProjectIdUseCase.execute(projectId)
        require(states.isNotEmpty()) { "Please create state first" }

        printTitle("Select Your Task Progression State")
        consoleIO.printer.printText(states.map { it.name }.toString(), TextStyle.OPTION)
        val stateName = receiveStringInput("Enter Task State:")
        return states.first { it.name == stateName }
    }

    private fun getTaskByName(name: String, tasks: List<Task>): Task {
        return tasks.first { it.title == name }
    }

    private fun receiveStringInput(message: String): String {
        consoleIO.printer.printText(message, TextStyle.OPTION)
        return consoleIO.reader.readStringFromUser()
    }

    private fun Task.showTaskDetails() {
        printTitle(title)
        printInfo(description)
        printInfo(currentProgressionState.name)
    }

    // Helper methods for consistent printing
    private fun printTitle(text: String) = consoleIO.printer.printText(text, TextStyle.TITLE)
    private fun printInfo(text: String) = consoleIO.printer.printText(text, TextStyle.INFO)
    private fun printError(text: String) = consoleIO.printer.printText(text, TextStyle.ERROR)
    private fun printSuccess(text: String) = consoleIO.printer.printText(text, TextStyle.SUCCESS)
}