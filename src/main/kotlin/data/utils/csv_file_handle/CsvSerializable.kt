package net.thechance.data.utils.csv_file_handle

interface CsvSerializable {
    companion object {
        fun <T> fromCsv(record: String, factory: (List<String>) -> T): T {
            val fields = record.split(",").map { it.trim() }
            return factory(fields)
        }
    }

    fun toCsvFields(): List<String>
}