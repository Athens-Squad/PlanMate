@file:OptIn(ExperimentalUuidApi::class, ExperimentalUuidApi::class, ExperimentalUuidApi::class)

package data.aduit_log.data_source.localCsvFile.mapper

import data.aduit_log.data_source.localCsvFile.dto.AuditLogCsvDto
import logic.entities.AuditLog
import kotlin.uuid.ExperimentalUuidApi

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