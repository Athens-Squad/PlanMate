@file:OptIn(ExperimentalUuidApi::class)

package net.thechance.logic.validators.auditLogValidations

import logic.entities.AuditLog
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

interface AuditLogValidator {
	fun validateAuditLogFieldsNotBlank(auditLog: AuditLog): Boolean
}