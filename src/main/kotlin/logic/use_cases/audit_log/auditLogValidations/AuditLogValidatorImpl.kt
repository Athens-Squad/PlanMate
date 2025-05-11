@file:OptIn(ExperimentalUuidApi::class, ExperimentalUuidApi::class, ExperimentalUuidApi::class)

package net.thechance.logic.use_cases.audit_log.auditLogValidations

import logic.entities.AuditLog
import net.thechance.logic.exceptions.InvalidAuditLogFieldsException
import net.thechance.logic.exceptions.InvalidEntityIdForAuditLog
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class AuditLogValidatorImpl : AuditLogValidator {
	override fun validateBeforeCreation(auditLog: AuditLog): Boolean {
		return when {
			auditLog.checkIsFieldsAreBlank() -> throw InvalidAuditLogFieldsException()
			else -> { true }
		}
	}


	override fun validateAfterCreation(entityId: Uuid): Boolean {
		return when {
			entityId.toString().isBlank() -> throw InvalidEntityIdForAuditLog()
			else -> { true }
		}
	}

	private fun AuditLog.checkIsFieldsAreBlank(): Boolean {
		return entityId.toString().isBlank() || description.isBlank() || userName.isBlank()
	}
}