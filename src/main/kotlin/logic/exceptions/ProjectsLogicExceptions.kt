package net.thechance.logic.exceptions

open class ProjectsLogicExceptions(message: String) : Exception(message)

class NotAuthorizedUserException(message: String = "") : ProjectsLogicExceptions("Not authorized user : $message")
class InvalidProjectNameException : ProjectsLogicExceptions("Invalid project name")
class NoProjectFoundException : ProjectsLogicExceptions("No project found")
class InvalidUsernameForProjectException : ProjectsLogicExceptions("Invalid username for project")
class ProjectAlreadyExistException : ProjectsLogicExceptions("Project already exist")