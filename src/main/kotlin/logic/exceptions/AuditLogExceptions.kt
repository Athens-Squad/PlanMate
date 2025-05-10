package net.thechance.logic.exceptions

open class AuditLogExceptions(message: String): Exception(message)
class MissingAuditLogFieldsException : AuditLogExceptions("The audit log entry is missing required fields.")

