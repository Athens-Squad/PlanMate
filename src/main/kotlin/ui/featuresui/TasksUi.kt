package ui.featuresui

import logic.entities.ProgressionState
import logic.entities.Task
import logic.use_cases.task.TasksUseCases
import net.thechance.data.authentication.UserSession
import net.thechance.ui.options.tasks.EditTaskOptions
import net.thechance.ui.options.tasks.TaskOptions
import ui.io.ConsoleIO

class TasksUi(
    private val consoleIO: ConsoleIO,
    private val tasksUseCases: TasksUseCases,
    private val auditLogUi: AuditLogUi,
    private val session: UserSession
) {
    fun manageTasks(tasks: MutableList<Task>, projectId: String, progressionStates: List<ProgressionState>) {
        do {
            consoleIO.printer.printTitle("Select A Task :")
            var inputTaskName = consoleIO.reader.readStringFromUser()
            getTaskId(inputTaskName, tasks)
                .onSuccess { taskId ->
                    tasksUseCases.getTaskByIdUseCase.execute(taskId)
                        .onSuccess { task ->
                            consoleIO.printer.printPlainText(task.toString())
                            handleTaskOptions(task, projectId, progressionStates)
                        }
                        .onFailure {
                            consoleIO.printer.printError(it.message.toString())
                        }
                }
                .onFailure {
                    consoleIO.printer.printError(it.message.toString())
                }
            consoleIO.printer.printOption("0 : Back")
            inputTaskName = consoleIO.reader.readStringFromUser()

        } while (inputTaskName != "0")

    }

    private fun handleTaskOptions(task: Task, projectId: String, progressionStates: List<ProgressionState>) {
        do {
            consoleIO.printer.printTitle("Select Option (1 to 4):")
            consoleIO.printer.printOptions(TaskOptions.entries)

            val inputTaskOption = consoleIO.reader.readNumberFromUser()

            when (inputTaskOption) {
                TaskOptions.EDIT.optionNumber -> editTask(progressionStates, task, projectId)
                TaskOptions.SHOW_HISTORY.optionNumber -> {
                    auditLogUi.getTaskHistory(task.id)
                    auditLogUi.showHistoryOption()
                }

                TaskOptions.DELETE.optionNumber -> {
                    deleteTask(task)
                        .onSuccess {
                            consoleIO.printer.printCorrectOutput("Task Deleted Successfully")
                        }
                        .onFailure {
                            consoleIO.printer.printError(it.message.toString())
                        }
                }
            }
        } while (inputTaskOption != TaskOptions.BACK.optionNumber ||
            inputTaskOption != TaskOptions.DELETE.optionNumber
        )
    }

    fun createTask(progressionStates: List<ProgressionState>, projectId: String): Result<Unit> {
        consoleIO.printer.printTitle("Create Task")
        val taskName = receiveStringInput("Enter Task Name : ")
        val taskDescription = receiveStringInput("Enter Task Description : ")

        consoleIO.printer.printTitle("Select Your Task Progression State")

        consoleIO.printer.printOption(
            progressionStates.map {
                it.name
            }.toString()
        )
        val inputTaskState = consoleIO.reader.readStringFromUser()

        return tasksUseCases.createTaskUseCase.execute(
            Task(
                title = taskName,
                description = taskDescription,
                currentProgressionState = ProgressionState(
                    name = inputTaskState,
                    projectId = projectId
                ),
                projectId = projectId
            ),
            userName = session.currentUser.name
        )
    }

    private fun editTask(progressionStates: List<ProgressionState>, task: Task, projectId: String): Result<Unit> {
        consoleIO.printer.printTitle("Edit Task")

        consoleIO.printer.printTitle("Select your option (1 to 3) : ")

        consoleIO.printer.printOptions(EditTaskOptions.entries)

        val inputEditOption = consoleIO.reader.readNumberFromUser()

        return when (inputEditOption) {
            EditTaskOptions.NAME.optionNumber -> editTaskName(task)
            EditTaskOptions.DESCRIPTION.optionNumber -> editTaskDescription(task)
            EditTaskOptions.PROGRESSION_STATE.optionNumber -> editTaskProgressionState(
                task,
                progressionStates,
                projectId
            )

            else -> Result.failure(Exception("Invalid Input!"))
        }
    }

    private fun editTaskProgressionState(
        task: Task,
        progressionStates: List<ProgressionState>,
        projectId: String
    ): Result<Unit> {

        consoleIO.printer.printTitle("Select Your Task Progression State")

        consoleIO.printer.printOption(
            progressionStates.map {
                it.name
            }.toString()
        )

        val taskState = receiveStringInput("Enter New Task State : ")

        return tasksUseCases
            .updateTaskUseCase
            .execute(
                task.copy(
                    currentProgressionState = ProgressionState(
                        name = taskState,
                        projectId = projectId
                    )
                ),
                userName = session.currentUser.name
            )
    }

    private fun editTaskDescription(task: Task): Result<Unit> {
        val taskDescription = receiveStringInput("Enter New Task Description : ")

        return tasksUseCases
            .updateTaskUseCase
            .execute(
                task.copy(description = taskDescription),
                userName = session.currentUser.name
            )
    }

    private fun editTaskName(task: Task): Result<Unit> {
        val taskName = receiveStringInput("Enter New Task Name : ")

        return tasksUseCases
            .updateTaskUseCase
            .execute(
                task.copy(title = taskName),
                userName = session.currentUser.name
            )

    }

    private fun deleteTask(task: Task): Result<Unit> {
        return tasksUseCases
            .deleteTaskUseCase
            .execute(
                taskId = task.id,
                userName = session.currentUser.name
            )
    }


    private fun getTaskId(inputTaskName: String, tasks: List<Task>): Result<String> {
        return runCatching {
            tasks.first { it.title == inputTaskName }.id
        }
    }

    private fun receiveStringInput(message: String): String {
        consoleIO.printer.printOption(message)
        return consoleIO.reader.readStringFromUser()
    }

}