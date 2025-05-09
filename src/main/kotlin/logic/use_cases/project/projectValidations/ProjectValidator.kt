package net.thechance.logic.use_cases.project.projectValidations

import logic.entities.Project
import logic.entities.User

interface ProjectValidator {
    suspend fun checkIfValueIsBlank(value:String):Boolean
    suspend fun checkIfUserIsAdmin(user: User):Boolean
    suspend fun checkIfProjectAlreadyExistInRepository(projectId: String,projects:List<Project>):Boolean
//    suspend fun checkIfUserIsAdmin(user: User):Boolean
//    suspend fun checkIfUserIsProjectOwner(username: String, projectOwner: String): Boolean
//    suspend fun checkIfProjectExistInRepositoryAndReturn(projectId: String, action: suspend () -> List<Project>): Project
//
//    suspend fun checkIfUserAuthorized(userName: String, action: suspend (username: String) -> User): Boolean
}