package net.thechance.data.aduit_log_csvfile.data_source

import com.mongodb.kotlin.client.coroutine.MongoDatabase
import kotlinx.coroutines.flow.toList
import logic.entities.AuditLog
import com.mongodb.kotlin.client.coroutine.MongoCollection
import data.aduit_log_csvfile.data_source.AuditLogDataSource
import logic.use_cases.authentication.exceptions.InvalidCredentialsException
import org.bson.Document

class MongoAuditLogFileDataSource(
    private val database: MongoDatabase
) : AuditLogDataSource {
    private val collection: MongoCollection<AuditLog> = database.getCollection("audit_logs", AuditLog::class.java)
    override suspend fun createAuditLog(auditLog: AuditLog) {
        try {
            collection.insertOne(auditLog)
        } catch (e: Exception) {
            throw InvalidCredentialsException("Failed to create audit log")
        }    }

    override suspend fun getAuditLogs(): List<AuditLog> {
        try {
            return collection.find().toList()
        } catch (e: Exception) {
            throw InvalidCredentialsException("Failed to retrieve audit logs")
        }
    }

    override suspend fun clearLog() {
        try {
            collection.deleteMany(Document())
        } catch (e: Exception) {
            throw InvalidCredentialsException("Failed to clear audit log")
        }
    }

}