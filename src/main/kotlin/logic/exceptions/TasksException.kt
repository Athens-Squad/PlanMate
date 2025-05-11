package logic.exceptions


open class TasksException(message: String): Exception(message)

class NoProjectFoundForTaskException() :
	TasksException("No Project Found For Task")

class NoProgressionStateFoundForTaskException() :
	TasksException("No ProgressionState Found For Task")

class TaskNotFoundException() :
	TasksException("Task Not Found")

class TaskAlreadyExistsException() :
	TasksException("Task Already Exists")

class InvalidTaskFieldsException() :
	TasksException("Invalid Task Fields")