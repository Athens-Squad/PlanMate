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
            throw TaskCreationFailedException("No task found with ID '$taskId'.")
        }

    }

    override suspend fun doIfTaskNotExistsOrThrow(task: Task,  action: suspend() -> Unit) {
        try {
            tasksRepository.getTaskById(task.id)
            throw TaskCreationFailedException("A task with ID '${task.id}' already exists.")

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
            throw TaskUpdateFailedException("Task ID cannot be changed during an update.")

        //validate projectId
        if (task.projectId != updatedTask.projectId)
            throw TaskUpdateFailedException("Cannot change the project associated with this task.")

        validateTaskFieldsIsNotBlankOrThrow(updatedTask)

        validateTaskState(updatedTask.currentProgressionState, updatedTask.projectId)
    }

    private fun validateTaskFieldsIsNotBlankOrThrow(task: Task) {
        // Validate task fields (title, description, currentState)
        if (task.title.isBlank()) throw InvalidTaskException("Task title cannot be empty.")
        if (task.description.isBlank()) throw InvalidTaskException("Task description cannot be empty.")
        if (task.currentProgressionState.id.isBlank() || task.currentProgressionState.name.isBlank())
            throw InvalidTaskException("Progression state ID and name must not be blank.")

    }

    private suspend fun validateTaskTitleExists(task: Task) {
        //Check if the task exists in the repository

        val taskExists = tasksRepository.getTasksByProjectId(task.projectId)
            .find { it.title == task.title } != null
        if (taskExists) {
            throw InvalidTaskException("A task with the title '${task.title}' already exists in project '${task.projectId}'.")
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