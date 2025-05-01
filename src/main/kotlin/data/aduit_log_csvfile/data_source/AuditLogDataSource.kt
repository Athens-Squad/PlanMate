package data.aduit_log_csvfile.data_source

import logic.entities.AuditLog

interface AuditLogDataSource {
    fun createAuditLog(auditLog: AuditLog): Result<Unit>
    fun getAuditLogs(): Result<List<AuditLog>>
    fun clearLog(): Result<Unit>
}