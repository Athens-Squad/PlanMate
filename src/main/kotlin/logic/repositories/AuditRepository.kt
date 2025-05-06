package logic.repositories

import logic.entities.AuditLog

interface AuditRepository {
    suspend fun createAuditLog(auditLog: AuditLog)
    suspend fun getAuditLogs(): List<AuditLog>
    suspend fun clearLog()
}

