package net.thechance.logic.repositories

import java.security.KeyFactory

interface CsvSerializable {
    companion object {
        fun <T> fromCsv(record: String, factory: (List<String>) -> T): T {
            val fields = record.split(",").map { it.trim() }
            return factory(fields)
        }
    }

    fun toCsvFields(): List<String>
}