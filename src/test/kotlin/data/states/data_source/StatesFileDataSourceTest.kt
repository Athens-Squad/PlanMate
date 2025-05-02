package data.states.data_source

import com.google.common.truth.Truth.assertThat
import helper.task_helper.createDummyState
import io.mockk.*
import net.thechance.data.csv_file_handle.CsvFileHandler
import net.thechance.data.csv_file_handle.CsvFileParser
import net.thechance.data.states.data_source.StatesFileDataSource
import net.thechance.logic.entities.State
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test

class StatesFileDataSourceTest{

  private val statesFileHandler: CsvFileHandler = mockk(relaxed = true)
  private val csvFileParser: CsvFileParser<State> = mockk(relaxed = true)
 lateinit var  stateFileDataSource : StatesFileDataSource

  @BeforeEach
  fun setUp(){

    stateFileDataSource = StatesFileDataSource(statesFileHandler , csvFileParser)
  }

 @Test
 fun `should create state successfully`() {
  // Given
  val state = createDummyState.dummyState()
  val record = "1,Test State" // Example CSV representation
  every { csvFileParser.toCsvRecord(state) } returns record
  every { statesFileHandler.appendRecord(record) } returns Unit // Mock the append operation

  // When
  val result = stateFileDataSource.createState(state)

  // Then
  assertEquals(Result.success(Unit), result)
  verify { statesFileHandler.appendRecord(record) }
 }
@Test
fun `updateState should return failure if getStates fails`() {
 // Arrange
 every { stateFileDataSource.getStates() } returns Result.failure(Exception("Simulated error"))

 // Act
 val result = stateFileDataSource.updateState(State("2", "Updated B","44"))

 // Assert
 assertTrue(result.isFailure)
 verify(exactly = 0) { statesFileHandler.writeRecords(any()) }
}


 @Test
 fun `should delete state in file, when deleteStateFromCsvFile called`(){
  val state = createDummyState.dummyState()
  // When
  val result = stateFileDataSource.deleteState(state.id)

  // Then
 assertTrue(result.isSuccess)
  verify(exactly = 1) { statesFileHandler.readRecords() }
 }

 }