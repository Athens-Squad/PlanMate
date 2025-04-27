package net.thechance.logic.use_cases.project

data class ProjectUseCases(
    val createProjectUseCase: CreateProjectUseCase,
    val updateProjectUseCase: UpdateProjectUseCase,
    val getProjectByIdUseCase: GetProjectByIdUseCase,
    val getProjectsByUserIdUseCase: GetProjectsByUserIdUseCase,
    val getAllProjectsUseCase: GetAllProjectsUseCase,
    val deleteProjectUseCase: DeleteProjectUseCase
)
