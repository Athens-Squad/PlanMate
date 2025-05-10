package net.thechance.logic.use_cases.audit_log.auditLogValidations

import logic.entities.AuditLog

interface AuditLogValidator {
	fun validateBeforeCreation(auditLog: AuditLog): Boolean
	fun validateAfterCreation(entityId: String): Boolean
}