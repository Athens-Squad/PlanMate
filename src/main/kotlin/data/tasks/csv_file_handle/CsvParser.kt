package net.thechance.data.tasks.csv_file_handle

import net.thechance.logic.repositories.CsvSerializable

class CsvParser<T: CsvSerializable> (
    private val factory: (List<String>) -> T
){
    fun parseRecord(record: String): T{
       return CsvSerializable.fromCsv(record, factory)
    }

    fun toCsvRecord(entity: T): String{
        return entity.toCsvFields().joinToString(",")
    }
}