package logic.use_cases.task

import logic.use_cases.task.DeleteTaskUseCase
import logic.use_cases.task.UpdateTaskUseCase

data class TasksUseCases(
    val createTaskUseCase: CreateTaskUseCase,
    val updateTaskUseCase: UpdateTaskUseCase,
    val deleteTaskUseCase: DeleteTaskUseCase,
    val getTaskByIdUseCase: GetTaskByIdUseCase,
    val getTasksByProjectIdUseCase: GetTasksByProjectIdUseCase
)
