package data.aduit_log_csvfile.data_source

import logic.entities.AuditLog

interface AuditLogDataSource {
    suspend fun createAuditLog(auditLog: AuditLog)
    suspend fun getAuditLogs(): List<AuditLog>
    suspend fun clearLog()
}