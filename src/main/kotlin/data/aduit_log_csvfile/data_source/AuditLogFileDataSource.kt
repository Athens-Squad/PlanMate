package net.thechance.data.aduit_log_csvfile.data_source

import logic.entities.AuditLog
import net.thechance.data.csv_file_handle.CsvFileHandler
import net.thechance.data.csv_file_handle.CsvFileParser

class AuditLogFileDataSource(
    private val auditLogFileHandler: CsvFileHandler,
    private val csvFileParser: CsvFileParser<AuditLog>
) : AuditLogDataSource {
    override fun createAuditLog(auditLog: AuditLog): Result<Unit> {
        return runCatching {
            val record = csvFileParser.toCsvRecord(auditLog)
            auditLogFileHandler.appendRecord(record)
        }
    }

    override fun getAuditLogs(): Result<List<AuditLog>> {
        return runCatching {
            auditLogFileHandler.readRecords()
                .map { csvFileParser.parseRecord(it) }
        }
    }

    override fun clearLog(): Result<Unit> {
        return runCatching {
            auditLogFileHandler.writeRecords(emptyList())
        }
    }
}