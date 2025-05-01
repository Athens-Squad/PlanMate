package net.thechance.logic.use_cases.project

data class ProjectUseCases(
    val createProjectUseCase: CreateProjectUseCase,
    val updateProjectUseCase: UpdateProjectUseCase,
    val getProjectByProjectIdUseCase: GetProjectByProjectIdUseCase,
    val getProjectsByUsernameUseCase: GetProjectsByUsernameUseCase,
    val getAllProjectsUseCase: GetAllProjectsUseCase,
    val deleteProjectUseCase: DeleteProjectUseCase
)
