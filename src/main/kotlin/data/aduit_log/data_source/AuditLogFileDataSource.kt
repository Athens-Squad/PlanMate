package net.thechance.data.aduit_log.data_source

import logic.entities.AuditLog
import data.utils.csv_file_handle.CsvFileHandler
import data.utils.csv_file_handle.CsvFileParser
import net.thechance.data.aduit_log.dto.AuditLogCsvDto
import net.thechance.data.aduit_log.dto.AuditLogDto
import net.thechance.data.aduit_log.mappers.toAuditLog
import net.thechance.data.aduit_log.mappers.toAuditLogCsvDto

class AuditLogFileDataSource(
    private val auditLogFileHandler: CsvFileHandler,
    private val csvFileParser: CsvFileParser<AuditLogCsvDto>
) : AuditLogDataSource {

    override suspend fun createAuditLog(auditLog: AuditLog) {
        val record = csvFileParser.toCsvRecord(auditLog.toAuditLogCsvDto())
        auditLogFileHandler.appendRecord(record)
    }

    override suspend fun getAuditLogs(): List<AuditLog> {
        return auditLogFileHandler.readRecords()
            .map {
				csvFileParser
					.parseRecord(it)
					.toAuditLog()
			}
    }

    override suspend fun clearLog() {
        auditLogFileHandler.writeRecords(emptyList())
    }
}