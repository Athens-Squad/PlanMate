package net.thechance.data.aduit_log.data_source

import kotlinx.coroutines.flow.toList
import logic.entities.AuditLog
import com.mongodb.kotlin.client.coroutine.MongoCollection
import kotlinx.coroutines.flow.map
import net.thechance.data.aduit_log.dto.AuditLogDto
import org.bson.Document

class MongoAuditLogDataSource(
    private val auditLogCollection: MongoCollection<AuditLogDto>
) : AuditLogDataSource {
    override suspend fun createAuditLog(auditLog: AuditLog) {
        auditLogCollection.insertOne(AuditLogDto.fromAuditLog(auditLog))
    }

    override suspend fun getAuditLogs(): List<AuditLog> {
        return auditLogCollection.find()
            .map { it.toAuditLog() }
            .toList()
    }

    override suspend fun clearLog() {
        auditLogCollection.deleteMany(Document())
    }
}