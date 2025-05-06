package logic.use_cases.task.taskvalidations

import logic.entities.Task
import logic.repositories.ProjectsRepository
import logic.repositories.StatesRepository
import logic.repositories.TasksRepository
import logic.exceptions.TasksException
import logic.entities.ProgressionState

class TaskValidatorImpl(
    private val tasksRepository: TasksRepository,
    private val projectsRepository: ProjectsRepository,
    private val statesRepository: StatesRepository
) : TaskValidator {
    override fun doIfTaskExistsOrThrow(taskId: String, action: (Task) -> Unit) {
        try {
            val task = tasksRepository.getTaskById(taskId)
            action(task)
        } catch (_: Exception) {
            throw TasksException.CannotCompleteTaskOperationException("Cannot find the task!")
        }

    }

    override fun doIfTaskNotExistsOrThrow(task: Task, action: () -> Unit) {
        try {
            tasksRepository.getTaskById(task.id)
            throw TasksException.CannotCompleteTaskOperationException("There is existing task with same id")

        } catch (_: Exception) {
            action()
        }

    }

    override fun validateTaskBeforeCreation(task: Task) {
        //validate task exists
        validateTaskTitleExists(task)

        // Validate project exists
        validateProjectExists(task.projectId)

        validateTaskFieldsIsNotBlankOrThrow(task)

        // Validate that the current state belongs to the same project
        validateTaskState(task.currentProgressionState, task.projectId)
    }

    override fun validateTaskBeforeUpdating(task: Task, updatedTask: Task) {
        //validate taskId
        if (task.id != updatedTask.id)
            throw TasksException.CannotUpdateTaskException("Cannot change taskId!")

        //validate projectId
        if (task.projectId != updatedTask.projectId)
            throw TasksException.CannotUpdateTaskException("Cannot change project!")

        validateTaskFieldsIsNotBlankOrThrow(updatedTask)

        validateTaskState(updatedTask.currentProgressionState, updatedTask.projectId)
    }

    private fun validateTaskFieldsIsNotBlankOrThrow(task: Task) {
        // Validate task fields (title, description, currentState)
        if (task.title.isBlank()) throw TasksException.InvalidTaskException("Task title cannot be empty.")
        if (task.description.isBlank()) throw TasksException.InvalidTaskException("Task description cannot be empty.")
        if (task.currentProgressionState.id.isBlank() || task.currentProgressionState.name.isBlank())
            throw TasksException.InvalidTaskException("Task currentState cannot be empty.")

    }

    private fun validateTaskTitleExists(task: Task) {
        //Check if the task exists in the repository

        val taskExists = tasksRepository.getTasksByProjectId(task.projectId)
            .find { it.title == task.title } != null
        if (taskExists) {
            throw TasksException.InvalidTaskException("Task with the same title already exist in this project.")
        }

    }

    private fun validateProjectExists(projectId: String) {
        projectsRepository.getProjects().getOrNull()?.find { it.id == projectId }
            ?: throw TasksException.InvalidTaskException("Project with ID $projectId does not exist.")

    }

    private fun validateTaskState(currentProgressionState: ProgressionState, projectId: String) {
        statesRepository.getStates().getOrNull()
            ?.find { it.id == currentProgressionState.id && it.projectId == projectId }
            ?: throw TasksException.InvalidTaskException("State '${currentProgressionState.name}' is not valid for the given project.")

    }

}