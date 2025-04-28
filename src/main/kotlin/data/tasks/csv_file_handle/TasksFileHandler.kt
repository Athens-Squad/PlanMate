package net.thechance.data.tasks.csv_file_handle

import java.io.File

class TasksFileHandler(private val file: File) {

    fun readRecords(): List<String> {
        return if (file.exists()) file.readLines() else emptyList()
    }

    fun writeRecords(records: List<String>) {
        if (file.exists())
            file.writeText(records.joinToString("\n"))
    }

    fun appendRecord(record: String) {
        file.appendText("$record\n")
    }
}