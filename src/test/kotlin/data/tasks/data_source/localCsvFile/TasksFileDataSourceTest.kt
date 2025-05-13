package data.tasks.data_source.localCsvFile

import data.utils.csv_file_handle.CsvFileHandler
import data.utils.csv_file_handle.CsvFileParser
import helper.task_helper.FakeTask.fakeTask
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify
import jdk.internal.org.jline.utils.ShutdownHooks
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
    private val faketasksFileHandler: CsvFileHandler = mockk(relaxed = true)
    private val fakecsvFileParser: CsvFileParser<TaskCsvDto> = mockk(relaxed = true)
    lateinit var fakeTasksFileDataSource: TasksFileDataSource


    @BeforeEach
    fun setup() {
        fakeTasksFileDataSource = TasksFileDataSource(faketasksFileHandler, fakecsvFileParser)
    }

    @Test
    fun `when create task should append record to tasks file handler`() {
        //given
        val fakeRecord = "sample,csv,record"
        //when
        coEvery { fakecsvFileParser.toCsvRecord(fakeTask.toTaskCsvDto()) } returns fakeRecord
        //then
        coVerify(exactly = 1) { faketasksFileHandler.appendRecord(fakeRecord) }
    }

    @OptIn(ExperimentalUuidApi::class)

    @Test
    fun `when getTaskById should return task when it exists`() {
        runTest {
            val taskId = Uuid.random()
            val stateId = Uuid.random()
            val projectId = Uuid.random()
            val expectedTask = Task(
                id = taskId,
                name = "Existing Task",
                description = "This task exists.",
                currentProgressionState = ProgressionState(
                    stateId,
                    "TODO",
                    projectId
                ),
                projectId
            )

            coEvery { fakeTasksFileDataSource.getAllTasks() } returns listOf(expectedTask)

            val result = fakeTasksFileDataSource.getTaskById(taskId)

            assertEquals(expectedTask, result)
        }
    }
}


