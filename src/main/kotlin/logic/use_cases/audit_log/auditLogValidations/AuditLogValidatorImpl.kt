@file:OptIn(ExperimentalUuidApi::class, ExperimentalUuidApi::class, ExperimentalUuidApi::class,
	ExperimentalUuidApi::class, ExperimentalUuidApi::class, ExperimentalUuidApi::class, ExperimentalUuidApi::class
)

package net.thechance.logic.use_cases.audit_log.auditLogValidations

import logic.entities.AuditLog
import net.thechance.logic.exceptions.InvalidAuditLogFieldsException
import kotlin.uuid.ExperimentalUuidApi

class AuditLogValidatorImpl : AuditLogValidator {
	override fun validateAuditLogFieldsNotBlank(auditLog: AuditLog): Boolean {
		return when {
			auditLog.checkIsFieldsAreBlank() -> throw InvalidAuditLogFieldsException()
			else -> { true }
		}
	}

	private fun AuditLog.checkIsFieldsAreBlank(): Boolean {
		return entityId.toString().isBlank() || description.isBlank() || userName.isBlank()
	}
}