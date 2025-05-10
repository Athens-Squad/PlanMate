@file:OptIn(ExperimentalUuidApi::class)

package net.thechance.data.aduit_log.data_source.remote.mongo.mapper

import logic.entities.AuditLog
import logic.entities.EntityType
import net.thechance.data.aduit_log.data_source.remote.mongo.dto.AuditLogDto
import java.time.LocalDateTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

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

