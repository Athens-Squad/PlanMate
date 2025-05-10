@file:OptIn(ExperimentalUuidApi::class)

package logic.use_cases.project.projectValidations

import logic.entities.*
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

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
    projectId: Uuid,
    action: suspend (projectId: Uuid) -> List<Project>
): Boolean {
    return action(projectId).none { it.id == projectId }
}

suspend fun checkIfProjectExistInRepositoryAndReturn(
    projectId: Uuid,
    action: suspend () -> List<Project>
): Project? {
    return action().firstOrNull { it.id == projectId }
}