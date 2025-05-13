@file:OptIn(ExperimentalUuidApi::class, ExperimentalUuidApi::class, ExperimentalUuidApi::class)

package net.thechance.logic.validators.taskvalidations

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

	override fun validateTaskFieldsNotBlank(task: Task): Boolean {
		return when {
			task.checkIsFieldsAreBlank() -> throw InvalidTaskFieldsException()
			else -> { true }
		}
	}

	@OptIn(ExperimentalUuidApi::class)
	override suspend fun validateProjectExists(projectId: Uuid): Boolean {
		return when {
			checkIfProjectNotExists(projectId) -> throw NoProjectFoundForTaskException()
			else -> { true }
		}
	}

	override suspend fun validateProgressionStateExists(progressionStateId: Uuid): Boolean {
		return when {
			checkIfProgressionStatesNotExists(progressionStateId)-> throw NoProgressionStateFoundForTaskException()
			else -> { true }
		}
	}

	override suspend fun validateTaskNotExists(taskId: Uuid): Boolean {
		return when {
			getCurrentTask(taskId) != null -> throw TaskAlreadyExistsException()
			else -> { true }
		}
	}

	override suspend fun validateTaskAlreadyExists(taskId: Uuid): Boolean {
		return when {
			getCurrentTask(taskId) == null -> throw TaskNotFoundException()
			else -> { true }
		}
	}

	private fun Task.checkIsFieldsAreBlank(): Boolean {
		return id.toString().isBlank() || name.isBlank() || projectId.toString().isBlank()
	}

	private suspend fun getCurrentTask(taskId: Uuid): Task? {
		return tasksRepository.getAllTasks().firstOrNull { it.id == taskId }
	}

	private suspend fun checkIfProjectNotExists(projectId: Uuid): Boolean {
		return projectsRepository.getProjects().none { it.id == projectId }
	}

	private suspend fun checkIfProgressionStatesNotExists(progressionStateId: Uuid): Boolean {
		return statesRepository.getProgressionStates().none { it.id == progressionStateId }
	}
}
