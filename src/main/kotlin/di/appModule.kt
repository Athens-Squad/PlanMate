package net.thechance.di

import logic.entities.AuditLog
import net.thechance.data.aduit_log_csvfile.data_source.AuditLogDataSource
import net.thechance.data.aduit_log_csvfile.data_source.AuditLogFileDataSource
import net.thechance.data.csv_file_handle.CsvFileHandler
import net.thechance.data.csv_file_handle.CsvFileParser
import org.koin.core.qualifier.named
import org.koin.dsl.module
import java.io.File

val appModule = module {
    single(named("auditLogCsvFile")) { File("data_files/audit_log.csv") }

    single(named("AuditLogFileHandler")) { CsvFileHandler(get(named("auditLogCsvFile"))) }

    single(named("AuditLogFileParser")) { CsvFileParser(factory = AuditLog.Companion::fromCsv) }


    single<AuditLogDataSource> {
        AuditLogFileDataSource(
            auditLogFileHandler = get(named("AuditLogFileHandler")),
            csvFileParser = get(named("AuditLogFileParser"))
        )
    }
}
