package data.tasks.data_source.remote.mongo

import com.mongodb.kotlin.client.coroutine.MongoCollection
import data.tasks.data_source.remote.mongo.mapper.toTaskDto
import helper.task_helper.FakeTask
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import logic.entities.ProgressionState
import logic.entities.Task
import net.thechance.data.tasks.data_source.remote.mongo.MongoTaskDataSource
import net.thechance.data.tasks.data_source.remote.mongo.dto.TaskDto
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class MongoTaskDataSourceTest {


    private val faketaskCollection: MongoCollection<TaskDto> = mockk(relaxed = true)
    private var taskObject : Task = mockk(relaxed = true)
    lateinit var fakeMongoTaskDataSource: MongoTaskDataSource


    @BeforeEach
    fun SetUp() {

        fakeMongoTaskDataSource = MongoTaskDataSource(faketaskCollection)
    }

    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun `createTask should insert taskDto into taskCollection`() {
        runTest {
            //given
            val dummyTask = FakeTask.fakeTask
            val dummyTaskDto = FakeTask.fakeTaskDto
            //when
            fakeMongoTaskDataSource.createTask(dummyTask)
            //then
            coVerify { faketaskCollection.insertOne(dummyTaskDto) }
        }

    }

}