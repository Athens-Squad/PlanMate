@file:OptIn(ExperimentalUuidApi::class)

package ui.featuresui

import kotlinx.coroutines.*
import logic.entities.ProgressionState
import logic.entities.Task
import logic.use_cases.task.TasksUseCases
import net.thechance.data.authentication.UserSession
import net.thechance.ui.options.tasks.EditTaskOptions
import net.thechance.ui.options.tasks.TaskOptions
import net.thechance.ui.utils.TextStyle
import ui.io.ConsoleIO
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class TasksUi(
    private val consoleIO: ConsoleIO,
    private val tasksUseCases: TasksUseCases,
    private val auditLogUi: AuditLogUi,
    private val session: UserSession
) {
    private val exceptionHandler: CoroutineExceptionHandler = CoroutineExceptionHandler({ _, throwable: Throwable ->
        consoleIO.printer.printText(throwable.message.toString(),TextStyle.ERROR)
    })
    private val tasksCoroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob() + exceptionHandler)


    suspend fun manageTasks(tasks: MutableList<Task>, projectId: Uuid, progressionStates: List<ProgressionState>) {
        tasksCoroutineScope.launch {
            try {
                do {
                    consoleIO.printer.printText("Select A Task :",TextStyle.TITLE)
                    var inputTaskName = consoleIO.reader.readStringFromUser()
                    val taskId = getTaskId(inputTaskName, tasks)
                    val task = tasksUseCases.getTaskByIdUseCase.execute(taskId)

                    task.showTaskDetails()

                    handleTaskOptions(task, projectId, progressionStates)
                    consoleIO.printer.printText("0 : Back",TextStyle.OPTION)
                    inputTaskName = consoleIO.reader.readStringFromUser()

                } while (inputTaskName != "0")
            } catch (exception: Exception) {
                consoleIO.printer.printText(exception.message.toString(),TextStyle.ERROR)
            }
        }.join()
    }

    private fun handleTaskOptions(task: Task, projectId: Uuid, progressionStates: List<ProgressionState>) {
        try {
            do {
                consoleIO.printer.printText("Select Option (1 to 4):",TextStyle.TITLE)
                consoleIO.printer.printOptions(TaskOptions.entries)

                val inputTaskOption = consoleIO.reader.readNumberFromUser()

                when (inputTaskOption) {
                    TaskOptions.EDIT.optionNumber -> editTask(progressionStates, task, projectId)
                    TaskOptions.SHOW_HISTORY.optionNumber -> {
                        tasksCoroutineScope.launch {
                            try {
                                auditLogUi.getTaskHistory(task.id)
                                auditLogUi.showHistoryOption()
                            } catch (exception: Exception) {
                                consoleIO.printer.printText("Error : ${exception.message}",TextStyle.ERROR)
                            }
                        }
                    }

                    TaskOptions.DELETE.optionNumber -> {
                        deleteTask(task)
                        consoleIO.printer.printText("Task Deleted Successfully",TextStyle.SUCCESS)
                    }
                }
            } while (inputTaskOption != TaskOptions.BACK.optionNumber ||
                inputTaskOption != TaskOptions.DELETE.optionNumber
            )
        } catch (exception: Exception) {
            consoleIO.printer.printText(exception.message.toString(),TextStyle.ERROR)
        }
    }

    fun createTask(progressionStates: List<ProgressionState>, projectId: Uuid) {
        consoleIO.printer.printText("Create Task",TextStyle.TITLE)
        val taskName = receiveStringInput("Enter Task Name : ")
        val taskDescription = receiveStringInput("Enter Task Description : ")

        consoleIO.printer.printText("Select Your Task Progression State",TextStyle.TITLE)

        consoleIO.printer.printText(
            progressionStates.map {
                it.name
            }.toString(),
            TextStyle.OPTION
        )
        val inputTaskState = consoleIO.reader.readStringFromUser()

        val state = progressionStates.first { it.name == inputTaskState }
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

    private fun editTask(progressionStates: List<ProgressionState>, task: Task, projectId: Uuid) {
        consoleIO.printer.printText("Edit Task",TextStyle.TITLE)

        consoleIO.printer.printText("Select your option (1 to 3) : ",TextStyle.TITLE)

        consoleIO.printer.printOptions(EditTaskOptions.entries)

        when (val inputEditOption = consoleIO.reader.readNumberFromUser()) {
            EditTaskOptions.NAME.optionNumber -> editTaskName(task)
            EditTaskOptions.DESCRIPTION.optionNumber -> editTaskDescription(task)
            EditTaskOptions.PROGRESSION_STATE.optionNumber -> editTaskProgressionState(
                task,
                progressionStates,
                projectId
            )

            else -> throw IllegalArgumentException("Invalid input: $inputEditOption")
        }
    }

    private fun editTaskProgressionState(
        task: Task,
        progressionStates: List<ProgressionState>,
        projectId: Uuid
    ) {

        consoleIO.printer.printText("Select Your Task Progression State",TextStyle.TITLE)

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
                                projectId = projectId
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
        }
    }


    private fun getTaskId(inputTaskName: String, tasks: List<Task>): Uuid {
        return tasks.first { it.title == inputTaskName }.id

    }

    private fun receiveStringInput(message: String): String {
        consoleIO.printer.printText(message,TextStyle.OPTION)
        return consoleIO.reader.readStringFromUser()
    }

    private fun Task.showTaskDetails() {
        consoleIO.printer.printText(this.title,TextStyle.TITLE)
        consoleIO.printer.printText(this.description,TextStyle.INFO)
        consoleIO.printer.printText(this.currentProgressionState.name,TextStyle.INFO)
    }

}