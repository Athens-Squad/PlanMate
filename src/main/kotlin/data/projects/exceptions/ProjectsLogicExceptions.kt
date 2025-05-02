package data.projects.exceptions

sealed class ProjectsLogicExceptions(message: String): Exception(message) {
    class NotAuthorizedUserException: ProjectsLogicExceptions("Not authorized user")
    class InvalidProjectNameException: ProjectsLogicExceptions("Invalid project name")
    class NoProjectFoundException: ProjectsLogicExceptions("No project found")
    class InvalidUsernameForProjectException: ProjectsLogicExceptions("Invalid username for project")
    class ProjectAlreadyExistException: ProjectsLogicExceptions("Project already exist")
}