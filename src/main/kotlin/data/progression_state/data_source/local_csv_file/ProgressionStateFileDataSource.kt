package data.progression_state.data_source.local_csv_file

import data.csv_file_handle.CsvFileHandler
import data.csv_file_handle.CsvFileParser
import data.progression_state.data_source.ProgressionStateDataSource
import logic.entities.ProgressionState
import net.thechance.data.progression_state.dto.ProgressionStateDto
import net.thechance.data.progression_state.mappers.toProgressionState
import net.thechance.data.progression_state.mappers.toProgressionStateDto

class ProgressionStateFileDataSource(
	private val progressionStatesFileHandler: CsvFileHandler,
	private val csvFileParser: CsvFileParser<ProgressionStateDto>
) : ProgressionStateDataSource {

	override suspend fun createProgressionState(progressionState: ProgressionState) {
		val record = csvFileParser.toCsvRecord(progressionState.toProgressionStateDto())
		progressionStatesFileHandler.appendRecord(record)
	}

	override suspend fun updateProgressionState(progressionState: ProgressionState) {
		val updateState = getProgressionStates()
			.map { if (it.id == progressionState.id) progressionState else it }
		val updatedRecords = updateState.map { csvFileParser.toCsvRecord(it.toProgressionStateDto()) }
		progressionStatesFileHandler.writeRecords(updatedRecords)
	}

	override suspend fun deleteProgressionState(progressionStateId: String) {
		val updatedState = getProgressionStates()
			.filter { it.id != progressionStateId }
		val updatedRecords = updatedState.map { csvFileParser.toCsvRecord(it.toProgressionStateDto()) }
		progressionStatesFileHandler.writeRecords(updatedRecords)
	}

	override suspend fun getProgressionStates(): List<ProgressionState> {
		return progressionStatesFileHandler.readRecords()
			.map { csvFileParser.parseRecord(it).toProgressionState() }
	}

	override suspend fun getProgressionStatesByProjectId(projectId: String): List<ProgressionState> {
		return progressionStatesFileHandler.readRecords()
			.map { csvFileParser.parseRecord(it).toProgressionState() }
			.filter { it.projectId == projectId }
	}
}