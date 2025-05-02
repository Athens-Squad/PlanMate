package data.states.data_source
import data.csv_file_handle.CsvFileHandler
import data.csv_file_handle.CsvFileParser
import logic.entities.ProgressionState


class StatesFileDataSource(
    private val statesFileHandler: CsvFileHandler,
    private val csvFileParser: CsvFileParser<ProgressionState>
) : StatesDataSource {
    override fun createState(progressionState: ProgressionState): Result<Unit> {
        return runCatching {

            val record = csvFileParser.toCsvRecord(progressionState)
            statesFileHandler.appendRecord(record)
        }
    }

    override fun updateState(progressionState: ProgressionState): Result<Unit> {
        return runCatching {
            val updateState = getStates()
                .getOrThrow()
                .map { if (it.id == progressionState.id) progressionState else it }
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

    override fun getStates(): Result<List<ProgressionState>> {
        return runCatching {
            statesFileHandler.readRecords()
                .map {
                    csvFileParser.parseRecord(it)
                }
        }
    }
}