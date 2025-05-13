@file:OptIn(ExperimentalUuidApi::class)
package data.progression_state.data_source.localCsvFile
import com.google.common.truth.Truth.assertThat
import data.progression_state.data_source.localCsvFile.dto.ProgressionStateCsvDto
import data.progression_state.data_source.remote.mongo.mapper.toProgressionState
import data.utils.csv_file_handle.CsvFileHandler
import data.utils.csv_file_handle.CsvFileParser
import helper.progression_state_helper.FakeProgressionStateData.fakeProgressionState1
import io.mockk.*
import kotlinx.coroutines.test.runTest
import net.thechance.data.user.data_source.localCsvFile.mapper.toUserCsvDto
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.uuid.ExperimentalUuidApi


class ProgressionStateFileDataSourceTest {

    private lateinit var mockFileHandler: CsvFileHandler
    private lateinit var mockCsvParser: CsvFileParser<ProgressionStateCsvDto>
    private lateinit var progressionStateFileDataSource: ProgressionStateFileDataSource
    private val fakeProgressionState = fakeProgressionState1
    @OptIn(ExperimentalUuidApi::class)
    private val fakeCsvDto = ProgressionStateCsvDto(
        id = fakeProgressionState.id,
        name = fakeProgressionState.name,
        projectId = fakeProgressionState.projectId
    )
    @OptIn(ExperimentalUuidApi::class)
    private val fakeCsvRecord = "${fakeCsvDto.id},${fakeCsvDto.name},${fakeCsvDto.projectId}"

    @BeforeEach
    fun setUp() {
        mockFileHandler = mockk(relaxed = true)
        mockCsvParser = mockk(relaxed = true)
        progressionStateFileDataSource = ProgressionStateFileDataSource(
            progressionStatesFileHandler = mockFileHandler,
            csvFileParser = mockCsvParser
        )
    }

    @Test
    fun `createProgressionState should append record to file`() = runTest {
        //given
        coEvery { mockFileHandler.readRecords() } returns emptyList()
        every { mockCsvParser.toCsvRecord(fakeCsvDto) } returns fakeCsvRecord
        coEvery { mockFileHandler.appendRecord(fakeCsvRecord) } just Runs

        //when
        progressionStateFileDataSource.createProgressionState(fakeProgressionState.toProgressionState())

        coVerify { mockFileHandler.appendRecord(fakeCsvRecord) }
    }

    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun `updateProgressionState should rewrite file with updated record`() = runTest {
        // given
        val updatedState = fakeProgressionState.copy(name = "Updated Name")
        val updatedCsvDto = fakeCsvDto.copy(name = "Updated Name")
        val updatedRecord = "${updatedCsvDto.id},${updatedCsvDto.name},${updatedCsvDto.projectId}"

        coEvery { mockFileHandler.readRecords() } returns listOf(fakeCsvRecord)
        coEvery { mockCsvParser.parseRecord(fakeCsvRecord) } returns fakeCsvDto
        coEvery { mockCsvParser.toCsvRecord(updatedCsvDto) } returns updatedRecord

        // when
        progressionStateFileDataSource.updateProgressionState(updatedState.toProgressionState())

        // then
        coVerify(exactly = 1) { mockFileHandler.writeRecords(listOf(updatedRecord)) }
    }

    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun `deleteProgressionState should rewrite file without deleted record`() = runTest {
        // given
        coEvery { mockFileHandler.readRecords() } returns listOf(fakeCsvRecord)
        coEvery { mockCsvParser.parseRecord(fakeCsvRecord) } returns fakeCsvDto

        // when
        progressionStateFileDataSource.deleteProgressionState(fakeProgressionState.id)

        // then
        coVerify(exactly = 1) { mockFileHandler.writeRecords(emptyList()) }
    }

    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun `getProgressionStatesByProjectId should return filtered states`() = runTest {
        // given
        coEvery { mockFileHandler.readRecords() } returns listOf(fakeCsvRecord)
        coEvery { mockCsvParser.parseRecord(fakeCsvRecord) } returns fakeCsvDto

        // when
        val result = progressionStateFileDataSource.getProgressionStatesByProjectId(fakeProgressionState.projectId)

        // then
        assertThat(result).containsExactly(fakeProgressionState.toProgressionState())
    }
}