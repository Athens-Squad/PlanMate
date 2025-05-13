@file:OptIn(ExperimentalUuidApi::class)

package data.tasks.data_source.localCsvFile

import com.google.common.truth.Truth.assertThat
import data.progression_state.data_source.remote.mongo.mapper.toProgressionState
import data.utils.csv_file_handle.CsvFileHandler
import data.utils.csv_file_handle.CsvFileParser
import helper.progression_state_helper.createDummyState
import helper.task_helper.FakeTask
import io.mockk.*
import kotlinx.coroutines.test.runTest
import logic.entities.ProgressionState
import logic.entities.Task
import net.thechance.data.tasks.data_source.localCsvFile.TasksFileDataSource
import net.thechance.data.tasks.data_source.localCsvFile.dto.TaskCsvDto
import net.thechance.data.tasks.data_source.localCsvFile.mapper.toTaskCsvDto
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid


class TasksFileDataSourceTest {
    private val fakeTasksFileHandler: CsvFileHandler = mockk(relaxed = true)
    private val fakeCsvFileParser: CsvFileParser<TaskCsvDto> = mockk(relaxed = true)
    private lateinit var tasksFileDataSource: TasksFileDataSource
    private lateinit var fakeTask: Task
    private lateinit var fakeDto: TaskCsvDto
    private lateinit var csvRecord: String


    @BeforeEach
    fun setup() {
        fakeTask = FakeTask.fakeTask
        fakeDto = fakeTask.toTaskCsvDto()
        csvRecord =
            "${fakeDto.id},${fakeDto.name},${fakeDto.description},${fakeDto.currentProgressionState.id},${fakeDto.currentProgressionState.name},${fakeDto.projectId}"
        tasksFileDataSource = TasksFileDataSource(fakeTasksFileHandler, fakeCsvFileParser)
    }

    @Test
    fun `when create task should append record to tasks file handler`() = runTest {
        coEvery { fakeTasksFileHandler.readRecords() } returns emptyList()
        every { fakeCsvFileParser.toCsvRecord(fakeDto) } returns csvRecord
        coEvery { fakeTasksFileHandler.appendRecord(csvRecord) } just Runs

        tasksFileDataSource.createTask(fakeTask)

        coVerify { fakeTasksFileHandler.appendRecord(csvRecord) }
    }

    @OptIn(ExperimentalUuidApi::class)

    @Test
    fun `when getTaskById should return task when it exists`() = runTest {
        // given
        coEvery { fakeTasksFileHandler.readRecords() } returns listOf(csvRecord)
        coEvery { fakeCsvFileParser.parseRecord(csvRecord) } returns fakeDto

        // when
        val result = tasksFileDataSource.getTaskById(fakeTask.id)

        // then
        assertThat(result).isEqualTo(fakeTask)
    }


}