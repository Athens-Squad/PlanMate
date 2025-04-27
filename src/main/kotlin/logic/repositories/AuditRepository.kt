package logic.repositories

import logic.entities.AuditLog

interface AuditRepository {
    fun createAuditLog(auditLog: AuditLog)
    fun getAuditLogs(): List<AuditLog>
    fun clearLog()
}
