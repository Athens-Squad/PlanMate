package data.aduit_log.data_source.localCsvFile

import data.utils.csv_file_handle.CsvFileHandler
import data.utils.csv_file_handle.CsvFileParser
import logic.entities.AuditLog
import net.thechance.data.aduit_log.data_source.AuditLogDataSource
import data.aduit_log.data_source.localCsvFile.dto.AuditLogCsvDto
import data.aduit_log.data_source.localCsvFile.mapper.toAuditLog
import data.aduit_log.data_source.localCsvFile.mapper.toAuditLogCsvDto

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