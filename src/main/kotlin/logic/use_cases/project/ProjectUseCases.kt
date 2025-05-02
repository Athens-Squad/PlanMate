package logic.use_cases.project

data class ProjectUseCases(
    val createProjectUseCase: CreateProjectUseCase,
    val updateProjectUseCase: UpdateProjectUseCase,
    val getProjectByIdUseCase: GetProjectByIdUseCase,
    val getAllProjectsByUsernameUseCase: GetAllProjectsByUsernameUseCase,
    val deleteProjectUseCase: DeleteProjectUseCase
)
