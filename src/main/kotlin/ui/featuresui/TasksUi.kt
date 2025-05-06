package ui.featuresui

import logic.entities.ProgressionState
import logic.entities.Task
import logic.use_cases.state.GetStatesByProjectIdUseCase
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
        try {
            do {
                consoleIO.printer.printTitle("Select A Task :")
                var inputTaskName = consoleIO.reader.readStringFromUser()
                val taskId = getTaskId(inputTaskName, tasks)
                val task = tasksUseCases.getTaskByIdUseCase.execute(taskId)
                consoleIO.printer.printPlainText(task.toString())
                handleTaskOptions(task, projectId, progressionStates)

                consoleIO.printer.printOption("0 : Back")
                inputTaskName = consoleIO.reader.readStringFromUser()

            } while (inputTaskName != "0")
        } catch (exception: Exception) {
            consoleIO.printer.printError(exception.message.toString())
        }

    }

    private fun handleTaskOptions(task: Task, projectId: String, progressionStates: List<ProgressionState>) {
        try {
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
                        consoleIO.printer.printCorrectOutput("Task Deleted Successfully")
                    }
                }
            } while (inputTaskOption != TaskOptions.BACK.optionNumber ||
                inputTaskOption != TaskOptions.DELETE.optionNumber
            )
        } catch (exception: Exception) {
            consoleIO.printer.printError(exception.message.toString())
        }
    }

    fun createTask(progressionStates: List<ProgressionState>, projectId: String) {
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

        val state = progressionStates.first { it.name == inputTaskState }
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

    private fun editTask(progressionStates: List<ProgressionState>, task: Task, projectId: String) {
        consoleIO.printer.printTitle("Edit Task")

        consoleIO.printer.printTitle("Select your option (1 to 3) : ")

        consoleIO.printer.printOptions(EditTaskOptions.entries)

        val inputEditOption = consoleIO.reader.readNumberFromUser()

        when (inputEditOption) {
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
        projectId: String
    ) {

        consoleIO.printer.printTitle("Select Your Task Progression State")

        consoleIO.printer.printOption(
            progressionStates.map {
                it.name
            }.toString()
        )

        val taskState = receiveStringInput("Enter New Task State : ")

        tasksUseCases
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


    private fun editTaskDescription(task: Task) {
        val taskDescription = receiveStringInput("Enter New Task Description : ")

        tasksUseCases
            .updateTaskUseCase
            .execute(
                task.copy(description = taskDescription),
                userName = session.currentUser.name
            )
    }

    private fun editTaskName(task: Task) {
        val taskName = receiveStringInput("Enter New Task Name : ")

        tasksUseCases
            .updateTaskUseCase
            .execute(
                task.copy(title = taskName),
                userName = session.currentUser.name
            )

    }

    private fun deleteTask(task: Task) {
        tasksUseCases
            .deleteTaskUseCase
            .execute(
                taskId = task.id,
                userName = session.currentUser.name
            )
    }


    private fun getTaskId(inputTaskName: String, tasks: List<Task>): String {
        return tasks.first { it.title == inputTaskName }.id

    }

    private fun receiveStringInput(message: String): String {
        consoleIO.printer.printOption(message)
        return consoleIO.reader.readStringFromUser()
    }

}