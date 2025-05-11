@file:OptIn(ExperimentalUuidApi::class)

package logic.use_cases.task.taskvalidations

import logic.entities.Task
import logic.exceptions.*
import logic.repositories.ProgressionStateRepository
import logic.repositories.ProjectsRepository
import logic.repositories.TasksRepository
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class TaskValidatorImpl(
    private val tasksRepository: TasksRepository,
    private val projectsRepository: ProjectsRepository,
    private val statesRepository: ProgressionStateRepository
) : TaskValidator {
	override suspend fun validateTaskBeforeCreation(task: Task): Boolean {
		return when {
			!task.checkIsFieldsAreValid() -> throw InvalidTaskFieldsException()
			!task.checkIfProjectExists() -> throw NoProjectFoundForTaskException()
			!task.checkIfTaskProgressionStateExists() -> throw NoProgressionStateFoundForTaskException()
			task.checkIfTaskExists() -> throw TaskAlreadyExistsException()
			else -> { true }
		}
	}

	override suspend fun validateTaskAfterCreation(
		taskId: Uuid
	): Boolean {
		val task = tasksRepository.getAllTasks().find { it.id == taskId }
			?: throw TaskNotFoundException()

		return when {
			!task.checkIsFieldsAreValid() -> throw InvalidProgressionStateFieldsException()
			!task.checkIfProjectExists() -> throw NoProjectFoundForProgressionStateException()
			!task.checkIfTaskProgressionStateExists() -> throw NoProgressionStateFoundForTaskException()
			else -> { true }
		}
	}

	private fun Task.checkIsFieldsAreValid(): Boolean {
		return id.toString().isNotBlank() && title.isNotBlank() && projectId.toString().isNotBlank()
	}

	private suspend fun Task.checkIfTaskExists(): Boolean {
		return tasksRepository.getAllTasks().any { it.id == id }
	}

	private suspend fun Task.checkIfProjectExists(): Boolean {
		return projectsRepository.getProjects().any { it.id == projectId }
	}



	private suspend fun Task.checkIfTaskProgressionStateExists(): Boolean {
		return statesRepository.getProgressionStates().any { it.id == currentProgressionState.id }
	}
}
