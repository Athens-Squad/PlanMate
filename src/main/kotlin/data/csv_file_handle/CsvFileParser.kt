package net.thechance.data.csv_file_handle

import net.thechance.logic.CsvSerializable

class CsvFileParser<T: CsvSerializable> (
    private val factory: (List<String>) -> T
){
    fun parseRecord(record: String): T{
       return CsvSerializable.fromCsv(record, factory)
    }

    fun toCsvRecord(entity: T): String{
        return entity.toCsvFields().joinToString(",")
    }
}