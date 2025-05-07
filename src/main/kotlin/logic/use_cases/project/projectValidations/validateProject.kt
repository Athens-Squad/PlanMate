package logic.use_cases.project.projectValidations

import logic.entities.*

suspend fun checkIfUserAuthorized(
    userName: String,
    action: suspend (username: String) -> User
): Boolean {
    return action(userName).type == UserType.AdminUser
}

fun String.checkIfFieldIsValid(): Boolean {
    return this.isNotBlank()
}

fun checkIfUserIsProjectOwner(username: String, projectOwner: String): Boolean {
    return username == projectOwner
}

suspend fun checkIfProjectAlreadyExistInRepository(
    projectId: String,
    action: suspend (projectId: String) -> List<Project>
): Boolean {
    return action(projectId).none { it.id == projectId }
}

suspend fun checkIfProjectExistInRepositoryAndReturn(
    projectId: String,
    action: suspend () -> List<Project>
): Project? {
    return action().firstOrNull { it.id == projectId }
}