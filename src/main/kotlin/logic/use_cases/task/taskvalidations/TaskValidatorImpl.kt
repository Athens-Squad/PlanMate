package logic.use_cases.task.taskvalidations

import logic.entities.ProgressionState
import logic.entities.Task
import logic.exceptions.*
import logic.repositories.ProgressionStateRepository
import logic.repositories.ProjectsRepository
import logic.repositories.TasksRepository

class TaskValidatorImpl(
    private val tasksRepository: TasksRepository,
    private val projectsRepository: ProjectsRepository,
    private val statesRepository: ProgressionStateRepository
) : TaskValidator {
    override suspend fun doIfTaskExistsOrThrow(taskId: String, action: suspend (Task) -> Unit) {
        try {
            val task = tasksRepository.getTaskById(taskId)
            action(task)
        } catch (_: Exception) {
            throw CannotCompleteTaskOperationException("Cannot find the task!")
        }

    }

    override suspend fun doIfTaskNotExistsOrThrow(task: Task,  action: suspend() -> Unit) {
        try {
            tasksRepository.getTaskById(task.id)
            throw CannotCompleteTaskOperationException("There is existing task with same id")

        } catch (_: Exception) {
            action()
        }

    }

    override suspend fun validateTaskBeforeCreation(task: Task) {
        //validate task exists
        validateTaskTitleExists(task)

        // Validate project exists
        validateProjectExists(task.projectId)

        validateTaskFieldsIsNotBlankOrThrow(task)

        // Validate that the current state belongs to the same project
        validateTaskState(task.currentProgressionState, task.projectId)
    }

    override suspend fun validateTaskBeforeUpdating(task: Task, updatedTask: Task) {
        //validate taskId
        if (task.id != updatedTask.id)
            throw CannotUpdateTaskException("Cannot change taskId!")

        //validate projectId
        if (task.projectId != updatedTask.projectId)
            throw CannotUpdateTaskException("Cannot change project!")

        validateTaskFieldsIsNotBlankOrThrow(updatedTask)

        validateTaskState(updatedTask.currentProgressionState, updatedTask.projectId)
    }

    private fun validateTaskFieldsIsNotBlankOrThrow(task: Task) {
        // Validate task fields (title, description, currentState)
        if (task.title.isBlank()) throw InvalidTaskException("Task title cannot be empty.")
        if (task.description.isBlank()) throw InvalidTaskException("Task description cannot be empty.")
        if (task.currentProgressionState.id.isBlank() || task.currentProgressionState.name.isBlank())
            throw InvalidTaskException("Task currentState cannot be empty.")

    }

    private suspend fun validateTaskTitleExists(task: Task) {
        //Check if the task exists in the repository

        val taskExists = tasksRepository.getTasksByProjectId(task.projectId)
            .find { it.title == task.title } != null
        if (taskExists) {
            throw InvalidTaskException("Task with the same title already exist in this project.")
        }

    }

    private suspend fun validateProjectExists(projectId: String) {
        projectsRepository.getProjects().find { it.id == projectId }
            ?: throw InvalidTaskException("Project with ID $projectId does not exist.")

    }

    private suspend fun validateTaskState(currentProgressionState: ProgressionState, projectId: String) {
        statesRepository.getProgressionStates()
            .find { it.id == currentProgressionState.id && it.projectId == projectId }
            ?: throw InvalidTaskException("State '${currentProgressionState.name}' is not valid for the given project.")

    }

}