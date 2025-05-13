package net.thechance.data.aduit_log.repository

import logic.repositories.AuditRepository
import net.thechance.data.aduit_log.data_source.AuditLogDataSource

class AuditLogRepositoryImpl(
    private val auditLogDataSource: AuditLogDataSource
) : AuditRepository, AuditLogDataSource by auditLogDataSource
