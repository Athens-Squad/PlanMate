package net.thechance.logic.exceptions

open class AuditLogExceptions(message: String) : Exception(message)

class InvalidAuditLogFieldsException() :
	AuditLogExceptions("Invalid audit log fields")