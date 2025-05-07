package logic.exceptions



open class TasksException(message: String): Exception(message)

class InvalidTaskException(message: String?): TasksException("Invalid Task!:     $message")
class CannotCompleteTaskOperationException(message: String?): TasksException("Cannot Complete Task Operation. $message")
class CannotUpdateTaskException(message: String?): TasksException("Cannot update the Task!:   $message")
