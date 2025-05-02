package logic.use_cases.project.projectValidations

import logic.entities.*

fun checkIfUserAuthorized(userName: String, action: (username: String) -> Result<User>): Boolean {
    return action(userName).fold(
        onSuccess = { user ->
            user.type == UserType.AdminUser
        },
        onFailure = { false }
    )
}

fun String.checkIfFieldIsValid(): Boolean {
    return this.isNotBlank()
}

fun checkIfUserIsProjectOwner(username: String, projectOwner: String): Boolean {
    return username == projectOwner
}

fun checkIfProjectExistInRepositoryAndReturn(projectId: String, action: (projectId: String) -> Result<List<Project>>): Project? {
    return action(projectId).fold(
        onSuccess = { projects -> projects.firstOrNull { it.id == projectId } },
        onFailure = { null }
    )
}