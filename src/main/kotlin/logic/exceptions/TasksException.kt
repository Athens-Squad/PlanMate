package logic.exceptions



open class TasksException(message: String): Exception(message)

class InvalidTaskException(reason: String) :
    TasksException("Task validation failed: $reason")

class TaskCreationFailedException(reason: String) :
    TasksException("Task creation failed: $reason")

class TaskUpdateFailedException(reason: String) :
    TasksException("Task update failed: $reason")