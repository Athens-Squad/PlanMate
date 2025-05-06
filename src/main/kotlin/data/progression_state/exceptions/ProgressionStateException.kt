package net.thechance.data.progression_state.exceptions

sealed class ProgressionStateException(message: String): DomainException(message)

class NoProjectFoundForProgressionStateException() :
	ProgressionStateException("No Project Found For Progression State")

class ProgressionStateNotFoundException() :
	ProgressionStateException("Progression State Not Found")

class ProgressionStateAlreadyExistsException() :
	ProgressionStateException("Progression State Already Exists")

class InvalidProgressionStateFieldsException() :
	ProgressionStateException("Invalid Progression State Fields")