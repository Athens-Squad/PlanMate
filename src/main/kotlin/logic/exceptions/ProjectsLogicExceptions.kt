package net.thechance.logic.exceptions

open class ProjectsLogicExceptions(message: String) : Exception(message)

class NotAuthorizedUserException : ProjectsLogicExceptions("Not authorized user")
class InvalidProjectFieldsException : ProjectsLogicExceptions("Invalid project fields")
class NoProjectFoundException : ProjectsLogicExceptions("No project found")
class InvalidUsernameForProjectException : ProjectsLogicExceptions("Invalid username for project")
class ProjectAlreadyExistException : ProjectsLogicExceptions("Project already exist")