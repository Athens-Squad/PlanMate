package net.thechance.data.states.data_source
import net.thechance.data.csv_file_handle.CsvFileHandler
import net.thechance.data.csv_file_handle.CsvFileParser
import net.thechance.logic.entities.State

class StatesFileDataSource(
    private val statesFileHandler: CsvFileHandler,
    private val csvFileParser: CsvFileParser<State>
) : StatesDataSource {
    override fun createState(state: State): Result<Unit> {
        return runCatching {

            val record = csvFileParser.toCsvRecord(state)
            statesFileHandler.appendRecord(record)
        }
    }

    override fun updateState(state: State): Result<Unit> {
        return runCatching {
            val updateState = getStates()
                .getOrThrow()
                .map { if (it.id == state.id) state else it }
            val updatedRecords = updateState.map { csvFileParser.toCsvRecord(it) }
            statesFileHandler.writeRecords(updatedRecords)

        }
    }

    override fun deleteState(stateId: String): Result<Unit> {
        return runCatching {
            val updatedState = getStates()
                .getOrThrow()
                .filter { it.id != stateId }
            val updatedRecords = updatedState.map { csvFileParser.toCsvRecord(it) }
            statesFileHandler.writeRecords(updatedRecords)
        }
    }

    override fun getStates(): Result<List<State>> {
        return runCatching {
            statesFileHandler.readRecords()
                .map { csvFileParser.parseRecord(it) }
        }
    }
}