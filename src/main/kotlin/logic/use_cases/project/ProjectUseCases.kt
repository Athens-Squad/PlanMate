package net.thechance.logic.use_cases.project

data class ProjectUseCases(
    val createProjectUseCase: CreateProjectUseCase,
    val updateProjectUseCase: UpdateProjectUseCase,
    val getAllProjectsByUsernameUseCase: GetAllProjectsByUsernameUseCase,
    val deleteProjectUseCase: DeleteProjectUseCase
)
