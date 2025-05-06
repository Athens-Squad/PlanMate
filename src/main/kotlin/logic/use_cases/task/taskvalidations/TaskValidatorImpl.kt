package logic.use_cases.task.taskvalidations

import logic.entities.Task
import logic.repositories.ProjectsRepository
import logic.repositories.TasksRepository
import logic.exceptions.TasksException
import logic.entities.ProgressionState
import logic.repositories.ProgressionStateRepository

class TaskValidatorImpl(
    private val tasksRepository: TasksRepository,
    private val projectsRepository: ProjectsRepository,
    private val statesRepository: ProgressionStateRepository
): TaskValidator {
    override suspend fun doIfTaskExistsOrThrow(taskId: String, action: (Task) -> Unit) {
        tasksRepository.getTaskById(taskId)
            .onSuccess {
                action(it)
            }
            .onFailure {
                throw TasksException.CannotCompleteTaskOperationException("Cannot find the task!")
            }
    }

    override suspend fun doIfTaskNotExistsOrThrow(task: Task, action: () -> Unit) {
        tasksRepository.getTaskById(task.id)
            .onFailure {
                action()
            }
            .onSuccess {
                throw TasksException.CannotCompleteTaskOperationException("There is existing task with same id")
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
        val taskExists = tasksRepository.getTasksByProjectId(task.projectId).getOrThrow()
            .find { it.title == task.title } != null
        if (taskExists) {
            throw TasksException.InvalidTaskException("Task with the same title already exist in this project.")
        }

    }

    private suspend fun validateProjectExists(projectId: String) {
        projectsRepository.getProjects().find { it.id == projectId }
            ?: throw TasksException.InvalidTaskException("Project with ID $projectId does not exist.")

    }

    private suspend fun validateTaskState(currentProgressionState: ProgressionState, projectId: String) {
        statesRepository.getProgressionStates()
            .find { it.id == currentProgressionState.id && it.projectId == projectId }
            ?: throw TasksException.InvalidTaskException("State '${currentProgressionState.name}' is not valid for the given project.")

    }

}