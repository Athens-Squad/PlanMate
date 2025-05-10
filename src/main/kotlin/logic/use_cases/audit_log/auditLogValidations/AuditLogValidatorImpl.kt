package net.thechance.logic.use_cases.audit_log.auditLogValidations

import logic.entities.AuditLog
import net.thechance.logic.exceptions.InvalidAuditLogFieldsException
import net.thechance.logic.exceptions.InvalidEntityIdForAuditLog

class AuditLogValidatorImpl : AuditLogValidator {
	override fun validateBeforeCreation(auditLog: AuditLog): Boolean {
		return when {
			!auditLog.checkIsFieldsAreValid() -> throw InvalidAuditLogFieldsException()
			else -> { true }
		}
	}

	override fun validateAfterCreation(entityId: String): Boolean {
		return when {
			entityId.isBlank() -> throw InvalidEntityIdForAuditLog()
			else -> { true }
		}
	}

	private fun AuditLog.checkIsFieldsAreValid(): Boolean {
		return entityId.isNotBlank() && description.isNotBlank() && userName.isNotBlank()
	}
}