package net.thechance.logic.use_cases.task

import com.thechance.logic.usecases.task.DeleteTaskUseCase
import com.thechance.logic.usecases.task.UpdateTaskUseCase

data class TasksUseCases(
    val createTaskUseCase: CreateTaskUseCase,
    val updateTaskUseCase: UpdateTaskUseCase,
    val deleteTaskUseCase: DeleteTaskUseCase,
    val getTaskByIdUseCase: GetTaskByIdUseCase,
    val getTasksByProjectIdUseCase: GetTasksByProjectIdUseCase
)
