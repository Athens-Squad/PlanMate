package net.thechance.data.aduit_log_csvfile

import logic.entities.AuditLog
import net.thechance.logic.entities.EntityType
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AuditLogFileParser(private val formatter: DateTimeFormatter) {

    fun parse(line: String): AuditLog {
        val parts = line.split(",").map { it.trim() }
        if (parts.size != 5) throw IllegalArgumentException("Invalid log format")
        return AuditLog(
            entityType = EntityType.valueOf(parts[0]),
            entityId = parts[1],
            description = parts[2],
            userId = parts[3],
            createdAt = LocalDateTime.parse(parts[4], formatter)
        )
    }

    fun toCsv(auditLog: AuditLog): String {
        return "${auditLog.entityId},${auditLog.entityType},${auditLog.userId},${auditLog.description},${auditLog.createdAt}"
    }
}