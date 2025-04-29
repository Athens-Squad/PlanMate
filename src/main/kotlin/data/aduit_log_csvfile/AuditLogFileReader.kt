package net.thechance.data.aduit_log_csvfile

import java.io.File

class AuditLogFileReader(private val file: File) {
    fun readAuditLogs(): List<String> {
        return file.useLines { it.drop(1).toList() }
    }
}