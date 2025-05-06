package data.aduit_log_csvfile.repository

import logic.entities.AuditLog
import logic.repositories.AuditRepository
import data.aduit_log_csvfile.data_source.AuditLogDataSource

class AuditLogRepositoryImpl(
    private val auditLogDataSource: AuditLogDataSource
) : AuditRepository {

    override suspend fun createAuditLog(auditLog: AuditLog) {
        auditLogDataSource.createAuditLog(auditLog)
    }

    override suspend fun getAuditLogs(): List<AuditLog> {
        return auditLogDataSource.getAuditLogs()
    }

    override suspend fun clearLog() {
        auditLogDataSource.clearLog()
    }
}
