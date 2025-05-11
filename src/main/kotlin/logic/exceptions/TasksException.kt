package logic.exceptions



open class TasksException(message: String): Exception(message)

class InvalidTaskException(message: String?):
    TasksException("Task is invalid: ${message ?: "Unknown reason."}")

class CannotCompleteTaskOperationException(message: String?):
    TasksException("Cannot Complete Task Operation. $message")

class CannotUpdateTaskException(message: String?):
    TasksException("Cannot update the Task!:   $message")
