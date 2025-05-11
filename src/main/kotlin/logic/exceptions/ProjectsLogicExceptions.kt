package net.thechance.logic.exceptions

open class ProjectsLogicExceptions(message: String) : Exception(message)

class NotAuthorizedUserException(username: String = "") :
    ProjectsLogicExceptions("Access failed: User '$username' is not authorized to perform this action on the project.")


class InvalidProjectFieldsException :
    ProjectsLogicExceptions("Invalid project name: The project name must be non-empty and follow naming rules.")



class NoProjectFoundException :
    ProjectsLogicExceptions("Project not found: No project matches the provided criteria.")

class InvalidUsernameForProjectException :
    ProjectsLogicExceptions("Invalid user: The specified username is not valid for this project context.")

class ProjectAlreadyExistException :
    ProjectsLogicExceptions("Project creation failed: A project with the same name already exists.")