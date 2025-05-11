package logic.use_cases.task

data class TasksUseCases(
    val createTaskUseCase: CreateTaskUseCase,
    val updateTaskUseCase: UpdateTaskUseCase,
    val deleteTaskUseCase: DeleteTaskUseCase,
    val getTaskByIdUseCase: GetTaskByIdUseCase,
    val getTasksByProjectIdUseCase: GetTasksByProjectIdUseCase
)
