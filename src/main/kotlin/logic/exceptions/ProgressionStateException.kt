package logic.exceptions

open class ProgressionStateException(message: String): Exception(message)

class NoProjectFoundForProgressionStateException() :
	ProgressionStateException("No Project Found For Progression State")

class ProgressionStateNotFoundException() :
	ProgressionStateException("Progression State Not Found")

class ProgressionStateAlreadyExistsException() :
	ProgressionStateException("Progression State Already Exists")

class InvalidProgressionStateFieldsException() :
	ProgressionStateException("Invalid Progression State Fields")