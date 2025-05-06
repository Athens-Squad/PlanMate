package data.aduit_log_csvfile.data_source

import logic.entities.AuditLog
import data.csv_file_handle.CsvFileHandler
import data.csv_file_handle.CsvFileParser

class AuditLogFileDataSource(
    private val auditLogFileHandler: CsvFileHandler,
    private val csvFileParser: CsvFileParser<AuditLog>
) : AuditLogDataSource {
    override fun createAuditLog(auditLog: AuditLog) {
        val record = csvFileParser.toCsvRecord(auditLog)
        auditLogFileHandler.appendRecord(record)
    }

    override fun getAuditLogs(): List<AuditLog> {
        return auditLogFileHandler.readRecords()
            .map { csvFileParser.parseRecord(it) }
    }


    override fun clearLog() {
            auditLogFileHandler.writeRecords(emptyList())
    }
}