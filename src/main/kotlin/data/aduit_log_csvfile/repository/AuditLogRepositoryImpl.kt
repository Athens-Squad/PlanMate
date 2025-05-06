package data.aduit_log_csvfile.repository

import logic.entities.AuditLog
import logic.repositories.AuditRepository
import data.aduit_log_csvfile.data_source.AuditLogDataSource

class AuditLogRepositoryImpl(private val auditLogDataSource: AuditLogDataSource)
    :AuditRepository {
    override fun createAuditLog(auditLog: AuditLog) {
        return auditLogDataSource.createAuditLog(auditLog)
    }

    override fun getAuditLogs() : List<AuditLog>{
               return auditLogDataSource.getAuditLogs()
    }

    override fun clearLog() {
         return auditLogDataSource.clearLog()
    }

}