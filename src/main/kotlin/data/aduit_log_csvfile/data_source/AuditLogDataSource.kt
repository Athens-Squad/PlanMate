package data.aduit_log_csvfile.data_source

import logic.entities.AuditLog

interface AuditLogDataSource {
    fun createAuditLog(auditLog: AuditLog)
    fun getAuditLogs(): List<AuditLog>
    fun clearLog()
}