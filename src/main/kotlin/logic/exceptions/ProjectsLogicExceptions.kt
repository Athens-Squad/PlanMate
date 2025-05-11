package net.thechance.logic.exceptions

open class ProjectLogicException(message: String) : Exception(message)

class NotAuthorizedUserException(username: String = "") :
    ProjectLogicException("Access failed: User '$username' is not authorized to perform this action on the project.")


class InvalidProjectNameException :
    ProjectLogicException("Invalid project name: The project name must be non-empty and follow naming rules.")



class ProjectNotFoundException :
    ProjectLogicException("Project not found: No project matches the provided criteria.")

class InvalidUsernameForProjectException :
    ProjectLogicException("Invalid user: The specified username is not valid for this project context.")

class ProjectAlreadyExistsException :
    ProjectLogicException("Project creation failed: A project with the same name already exists.")

