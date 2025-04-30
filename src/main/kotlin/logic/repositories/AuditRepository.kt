package logic.repositories

import logic.entities.AuditLog

interface AuditRepository {
    fun createAuditLog(auditLog: AuditLog): Result<Unit>
    fun getAuditLogs(): Result<List<AuditLog>>
    fun clearLog(): Result<Unit>
}
