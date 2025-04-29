package logic.use_cases.task

import logic.entities.Task
import logic.repositories.AuditRepository
import logic.repositories.ProjectsRepository
import logic.repositories.TasksRepository


class UpdateTaskUseCase(
    private val taskRepository: TasksRepository,
    private val projectsRepository: ProjectsRepository,
    private val auditRepository: AuditRepository
) {

    fun execute(updatedTask: Task) {
        //check projectId
        taskRepository.getTaskById(updatedTask.id)
            .onSuccess {
                taskRepository.updateTask(updatedTask)
            }
            .onFailure { throwable ->
                throw NoSuchElementException("Cannot update!:\n    ${throwable.message}")
            }
    }
}
