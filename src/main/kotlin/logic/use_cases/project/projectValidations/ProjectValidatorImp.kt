package net.thechance.logic.use_cases.project.projectValidations

import logic.entities.Project
import logic.entities.User
import logic.entities.UserType

class ProjectValidatorImp():ProjectValidator {

    override suspend fun checkIfValueIsBlank(value: String): Boolean {
        return value.isBlank()
    }

    override suspend fun checkIfUserIsAdmin(user: User): Boolean {
        return user.type == UserType.AdminUser
    }

    override suspend fun checkIfProjectAlreadyExistInRepository(projectId: String, projects: List<Project>): Boolean {
       return projects.any { it.id == projectId }
    }

    //    override suspend fun checkIfUserAuthorized(
//        userName: String,
//        action: suspend (username: String) -> User
//    ): Boolean {
//        return action(userName).type == UserType.AdminUser
//    }

//    override suspend fun checkIfUserIsProjectOwner(username: String, projectOwner: String): Boolean {
//        return username == projectOwner
//    }
//
//    override suspend fun checkIfProjectAlreadyExistInRepository(
//        projectId: String,
//        action: suspend (projectId: String) -> List<Project>
//    ): Boolean {
//        return action(projectId).none { it.id == projectId }
//    }
//
//    override suspend fun checkIfProjectExistInRepositoryAndReturn(
//        projectId: String,
//        action: suspend () -> List<Project>
//    ): Project {
//        return action().firstOrNull { it.id == projectId }
//    }

}