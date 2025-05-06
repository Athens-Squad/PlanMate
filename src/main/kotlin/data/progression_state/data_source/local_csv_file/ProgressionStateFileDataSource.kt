package data.progression_state.data_source.local_csv_file

import data.csv_file_handle.CsvFileHandler
import data.csv_file_handle.CsvFileParser
import data.progression_state.data_source.ProgressionStateDataSource
import logic.entities.ProgressionState

class ProgressionStateFileDataSource(
	private val progressionStatesFileHandler: CsvFileHandler,
	private val csvFileParser: CsvFileParser<ProgressionState>
) : ProgressionStateDataSource {

	override suspend fun createProgressionState(progressionState: ProgressionState) {
		val record = csvFileParser.toCsvRecord(progressionState)
		progressionStatesFileHandler.appendRecord(record)
	}

	override suspend fun updateProgressionState(progressionState: ProgressionState) {
		val updateState = getProgressionStates()
			.map { if (it.id == progressionState.id) progressionState else it }
		val updatedRecords = updateState.map { csvFileParser.toCsvRecord(it) }
		progressionStatesFileHandler.writeRecords(updatedRecords)
	}

	override suspend fun deleteProgressionState(progressionStateId: String) {
		val updatedState = getProgressionStates()
			.filter { it.id != progressionStateId }
		val updatedRecords = updatedState.map { csvFileParser.toCsvRecord(it) }
		progressionStatesFileHandler.writeRecords(updatedRecords)
	}

	override suspend fun getProgressionStates(): List<ProgressionState> {
		return progressionStatesFileHandler.readRecords()
			.map { csvFileParser.parseRecord(it) }
	}

	override suspend fun getProgressionStatesByProjectId(projectId: String): List<ProgressionState> {
		return progressionStatesFileHandler.readRecords()
			.map { csvFileParser.parseRecord(it) }
			.filter { it.projectId == projectId }
	}
}