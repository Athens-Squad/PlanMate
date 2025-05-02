package data.aduit_log_csvfile.repository

import logic.entities.AuditLog
import logic.repositories.AuditRepository
import data.aduit_log_csvfile.data_source.AuditLogDataSource

class AuditLogRepositoryImpl(private val auditLogDataSource: AuditLogDataSource)
    :AuditRepository {
    override fun createAuditLog(auditLog: AuditLog): Result<Unit> {
        return auditLogDataSource.createAuditLog(auditLog)
    }

    override fun getAuditLogs(): Result<List<AuditLog>> {
               return auditLogDataSource.getAuditLogs()
    }

    override fun clearLog(): Result<Unit> {
         return auditLogDataSource.clearLog()
    }

}