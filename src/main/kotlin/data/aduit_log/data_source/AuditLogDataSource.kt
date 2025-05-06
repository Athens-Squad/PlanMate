package net.thechance.data.aduit_log.data_source

import logic.entities.AuditLog

interface AuditLogDataSource {
    suspend fun createAuditLog(auditLog: AuditLog)
    suspend fun getAuditLogs(): List<AuditLog>
    suspend fun clearLog()
}