package net.thechance.data.aduit_log.mappers

import logic.entities.AuditLog
import logic.entities.EntityType
import net.thechance.data.aduit_log.dto.AuditLogCsvDto
import net.thechance.data.aduit_log.dto.AuditLogDto
import java.time.LocalDateTime

fun AuditLogDto.toAuditLog() = AuditLog(
	id = id,
	entityType = EntityType.valueOf(entityType),
	entityId = entityId,
	description = description,
	userName = userName,
	createdAt = LocalDateTime.parse(createdAt)
)

fun AuditLog.toAuditLogDto() = AuditLogDto(
	id = id,
	entityType = entityType.name,
	entityId = entityId,
	description = description,
	userName = userName,
	createdAt = createdAt.toString()
)

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