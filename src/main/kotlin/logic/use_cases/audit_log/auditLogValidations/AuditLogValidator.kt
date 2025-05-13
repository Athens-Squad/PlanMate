@file:OptIn(ExperimentalUuidApi::class)

package net.thechance.logic.use_cases.audit_log.auditLogValidations

import logic.entities.AuditLog
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

interface AuditLogValidator {
	fun validateAuditLogFieldsNotBlank(auditLog: AuditLog): Boolean
}