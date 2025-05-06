package net.thechance.data.aduit_log.repository

import logic.entities.AuditLog
import logic.repositories.AuditRepository
import net.thechance.data.aduit_log.data_source.AuditLogDataSource

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
