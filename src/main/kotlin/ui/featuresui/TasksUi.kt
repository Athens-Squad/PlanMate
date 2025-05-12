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

class TasksUi(
    private val consoleIO: ConsoleIO,
    private val tasksUseCases: TasksUseCases,
    private val getProgressionStatesByProjectIdUseCase: GetProgressionStatesByProjectIdUseCase,
    private val auditLogUi: AuditLogUi,
    private val session: UserSession
) {
    private val exceptionHandler: CoroutineExceptionHandler = CoroutineExceptionHandler { _, throwable: Throwable ->
        consoleIO.printer.printText(throwable.message.toString(), TextStyle.ERROR)
    }
    private val tasksCoroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob() + exceptionHandler)


    suspend fun manageTasks(tasks: List<Task>, projectId: Uuid, progressionStates: List<ProgressionState>) {
        tasksCoroutineScope.launch {
            try {
                do {
                    consoleIO.printer.printText("Select A Task :", TextStyle.TITLE)
                    consoleIO.printer.printText("0 : Back", TextStyle.OPTION)

                    val inputTaskName = consoleIO.reader.readStringFromUser()
                    if (inputTaskName == "0") return@launch

                    val task = getTaskByName(inputTaskName, tasks)
                    task.showTaskDetails()
                    handleTaskOptions(task, progressionStates)

                } while (true)
            } catch (exception: Exception) {
                consoleIO.printer.printText(exception.message.toString(), TextStyle.ERROR)
            }
        }.join()
    }

    suspend fun createTask(
        projectId: Uuid,
        progressionStates: List<ProgressionState>
    ) {
        consoleIO.printer.printText("Create Task", TextStyle.TITLE)
        val taskName = receiveStringInput("Enter Task Name : ")
        val taskDescription = receiveStringInput("Enter Task Description : ")
        val state = selectProgressionState(progressionStates)

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

    private suspend fun handleTaskOptions(task: Task, progressionStates: List<ProgressionState>) {
        try {
            do {
                consoleIO.printer.printText("Select Option (1 to 4):", TextStyle.TITLE)
                consoleIO.printer.printOptions(TaskOptions.entries)

                val option = consoleIO.reader.readNumberFromUser()
                when (option) {
                    TaskOptions.EDIT.optionNumber -> editTask(progressionStates, task)

                    TaskOptions.SHOW_HISTORY.optionNumber -> auditLogUi.showTaskHistory(task.id)

                    TaskOptions.DELETE.optionNumber -> deleteTask(task)
                }
            } while (option != TaskOptions.BACK.optionNumber ||
                option != TaskOptions.DELETE.optionNumber
            )
        } catch (exception: Exception) {
            consoleIO.printer.printText(exception.message.toString(), TextStyle.ERROR)
        }
    }



    private fun editTask(progressionStates: List<ProgressionState>, task: Task) {
        consoleIO.printer.printText("Edit Task", TextStyle.TITLE)

        consoleIO.printer.printText("Select your option (1 to 3) : ", TextStyle.TITLE)

        consoleIO.printer.printOptions(EditTaskOptions.entries)

        when (val inputEditOption = consoleIO.reader.readNumberFromUser()) {
            EditTaskOptions.NAME.optionNumber -> editTaskName(task)
            EditTaskOptions.DESCRIPTION.optionNumber -> editTaskDescription(task)
            EditTaskOptions.PROGRESSION_STATE.optionNumber -> editTaskProgressionState(
                task,
                progressionStates
            )

            else -> throw IllegalArgumentException("Invalid input: $inputEditOption")
        }
    }

    private fun editTaskProgressionState(
        task: Task,
        progressionStates: List<ProgressionState>
    ) {

        consoleIO.printer.printText("Select Your Task Progression State", TextStyle.TITLE)

        consoleIO.printer.printText(
            progressionStates.map {
                it.name
            }.toString(),
            TextStyle.OPTION
        )

        val taskState = receiveStringInput("Enter New Task State : ")
        tasksCoroutineScope.launch {
            tasksUseCases
                .updateTaskUseCase
                .execute(
                    task.copy(
                        currentProgressionState =
                            task.currentProgressionState.copy(
                                name = taskState,
                                projectId = task.projectId
                            )
                    ),
                    userName = session.currentUser.name
                )
        }
    }


    private fun editTaskDescription(task: Task) {
        val taskDescription = receiveStringInput("Enter New Task Description : ")

        tasksCoroutineScope.launch {
            tasksUseCases
                .updateTaskUseCase
                .execute(
                    task.copy(description = taskDescription),
                    userName = session.currentUser.name
                )
        }
    }

    private fun editTaskName(task: Task) {
        val taskName = receiveStringInput("Enter New Task Name : ")

        tasksCoroutineScope.launch {

            tasksUseCases
                .updateTaskUseCase
                .execute(
                    task.copy(title = taskName),
                    userName = session.currentUser.name
                )
        }

    }

    private fun deleteTask(task: Task) {
        tasksCoroutineScope.launch {
            tasksUseCases
                .deleteTaskUseCase
                .execute(
                    taskId = task.id,
                    userName = session.currentUser.name
                )
            consoleIO.printer.printText("Task Deleted Successfully", TextStyle.SUCCESS)
        }
    }

    private fun getTaskByName(name: String, tasks: List<Task>): Task {
        return tasks.first { it.title == name }
    }

    private suspend fun selectProgressionState(progressionStates: List<ProgressionState>): ProgressionState {
        require(progressionStates.isNotEmpty()) { "Please create state first" }

        consoleIO.printer.printText("Select Your Task Progression State", TextStyle.TITLE)
        consoleIO.printer.printText(
            progressionStates.map { it.name }.toString(),
            TextStyle.OPTION
        )
        val stateName = receiveStringInput("Enter Task State:")
        return progressionStates.first { it.name == stateName }
    }

    private fun getTaskId(inputTaskName: String, tasks: List<Task>): Uuid {
        return tasks.first { it.title == inputTaskName }.id

    }

    private fun receiveStringInput(message: String): String {
        consoleIO.printer.printText(message, TextStyle.OPTION)
        return consoleIO.reader.readStringFromUser()
    }

    private fun Task.showTaskDetails() {
        consoleIO.printer.printText(this.title, TextStyle.TITLE)
        consoleIO.printer.printText(this.description, TextStyle.INFO)
        consoleIO.printer.printText(this.currentProgressionState.name, TextStyle.INFO)
    }

}