@file:OptIn(ExperimentalUuidApi::class)

package data.aduit_log.data_source.localCsvFile.mapper

import logic.entities.AuditLog
import data.aduit_log.data_source.localCsvFile.dto.AuditLogCsvDto
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

fun AuditLogCsvDto.toAuditLog() = AuditLog(
	id = id,
	entityType = entityType,
	entityId = entityId,
	description = description,
	userName = userName,
	createdAt = createdAt
)

fun AuditLog.toAuditLogCsvDto() = AuditLogCsvDto(
	id = id,
	entityType = entityType,
	entityId = entityId,
	description = description,
	userName = userName,
	createdAt = createdAt
)