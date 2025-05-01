package net.thechance.logic.use_cases.project.validations

import logic.entities.Project
import logic.entities.Task
import logic.entities.User
import net.thechance.logic.entities.State
import net.thechance.logic.entities.UserType

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

fun checkIfProjectsStatesValid(projectId: String, action: () -> Result<List<State>>): Boolean {
    return action().fold(
        onSuccess = { states -> states.any { it.projectId == projectId } },
        onFailure = { false }
    )
}

fun checkIfProjectsTasksValid(projectId: String, action: (projectId: String) -> Result<List<Task>>): Boolean {
    return action(projectId).fold(
        onSuccess = { it.isNotEmpty() },
        onFailure = { false }
    )
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