package net.thechance.logic.use_cases.project.exceptions

sealed class ProjectsLogicExceptions(message: String): Exception(message) {
    class NotAuthorizedUserException: ProjectsLogicExceptions("Not authorized user")
    class InvalidProjectNameException: ProjectsLogicExceptions("Invalid project name")
    class NoStatesFoundForProjectException: ProjectsLogicExceptions("Invalid project states")
    class NoTasksFoundForProjectException: ProjectsLogicExceptions("Invalid project tasks")
    class NoProjectFoundException: ProjectsLogicExceptions("No project found")
    class InvalidUsernameForProjectException: ProjectsLogicExceptions("Invalid username for project")
}