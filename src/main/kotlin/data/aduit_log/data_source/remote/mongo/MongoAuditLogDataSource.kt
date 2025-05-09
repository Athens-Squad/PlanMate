package net.thechance.data.aduit_log.data_source.remote.mongo

import com.mongodb.kotlin.client.coroutine.MongoCollection
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import logic.entities.AuditLog
import net.thechance.data.aduit_log.data_source.AuditLogDataSource
import net.thechance.data.aduit_log.data_source.remote.mongo.dto.AuditLogDto
import net.thechance.data.aduit_log.data_source.remote.mongo.mapper.toAuditLog
import net.thechance.data.aduit_log.data_source.remote.mongo.mapper.toAuditLogDto
import org.bson.Document

class MongoAuditLogDataSource(
	private val auditLogCollection: MongoCollection<AuditLogDto>
) : AuditLogDataSource {
	override suspend fun createAuditLog(auditLog: AuditLog) {
		auditLogCollection.insertOne(auditLog.toAuditLogDto())
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