package data.progression_state.data_source.localCsvFile

import data.progression_state.data_source.ProgressionStateDataSource
import data.utils.csv_file_handle.CsvFileHandler
import data.utils.csv_file_handle.CsvFileParser
import logic.entities.ProgressionState
import data.progression_state.data_source.localCsvFile.dto.ProgressionStateCsvDto
import data.progression_state.data_source.localCsvFile.mapper.toProgressionState
import data.progression_state.data_source.localCsvFile.mapper.toProgressionStateCsvDto

class ProgressionStateFileDataSource(
	private val progressionStatesFileHandler: CsvFileHandler,
	private val csvFileParser: CsvFileParser<ProgressionStateCsvDto>
) : ProgressionStateDataSource {

	override suspend fun createProgressionState(progressionState: ProgressionState) {
		val record = csvFileParser.toCsvRecord(progressionState.toProgressionStateCsvDto())
		progressionStatesFileHandler.appendRecord(record)
	}

	override suspend fun updateProgressionState(progressionState: ProgressionState) {
		val updateState = getProgressionStates()
			.map { if (it.id == progressionState.id) progressionState else it }
		val updatedRecords = updateState.map {
			csvFileParser.toCsvRecord(it.toProgressionStateCsvDto())
		}
		progressionStatesFileHandler.writeRecords(updatedRecords)
	}

	override suspend fun deleteProgressionState(progressionStateId: String) {
		val updatedState = getProgressionStates()
			.filter { it.id != progressionStateId }
		val updatedRecords = updatedState.map {
			csvFileParser.toCsvRecord(it.toProgressionStateCsvDto())
		}
		progressionStatesFileHandler.writeRecords(updatedRecords)
	}

	override suspend fun getProgressionStates(): List<ProgressionState> {
		return progressionStatesFileHandler.readRecords()
			.map {
				csvFileParser.parseRecord(it)
					.toProgressionState()
			}
	}

	override suspend fun getProgressionStatesByProjectId(projectId: String): List<ProgressionState> {
		return progressionStatesFileHandler.readRecords()
			.map {
				csvFileParser.parseRecord(it)
					.toProgressionState()
			}
			.filter { it.projectId == projectId }
	}
}