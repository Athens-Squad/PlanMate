@file:OptIn(ExperimentalUuidApi::class)

package data.tasks.data_source.remote.mongo

import com.google.common.truth.Truth.assertThat
import com.mongodb.kotlin.client.coroutine.FindFlow
import com.mongodb.kotlin.client.coroutine.MongoCollection
import data.tasks.data_source.remote.mongo.mapper.toTask
import data.tasks.data_source.remote.mongo.mapper.toTaskDto
import helper.task_helper.FakeTask
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import logic.entities.Task
import net.thechance.data.tasks.data_source.remote.mongo.MongoTaskDataSource
import net.thechance.data.tasks.data_source.remote.mongo.dto.TaskDto
import org.bson.conversions.Bson
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.test.BeforeTest
import kotlin.uuid.ExperimentalUuidApi

class MongoTaskDataSourceTest {

    private lateinit var taskCollection: MongoCollection<TaskDto>
    private lateinit var dataSource: MongoTaskDataSource
    private lateinit var task: Task
    private lateinit var dto: TaskDto

    @BeforeTest
    fun setup() {
        taskCollection = mockk<MongoCollection<TaskDto>>()
        dataSource = MongoTaskDataSource(taskCollection)
        task = FakeTask.fakeTask // You can adjust this as needed for your FakeTask
        dto = task.toTaskDto()
    }

    @Test
    fun `should return empty list when no tasks exist for project ID`() = runTest {
        // Given
        val projectId = task.projectId
        val findFlowMock = mockk<FindFlow<TaskDto>>(relaxed = true)
        coEvery { findFlowMock.toList() } returns emptyList()
        every { taskCollection.find(any<Bson>()) } returns findFlowMock

        // When
        val result = dataSource.getTasksByProjectId(projectId)

        // Then
        assertThat(result).isEmpty()
    }

    @Test
    fun `should get all tasks`() = runTest {
        // Given
        val taskDtoList = listOf(dto, dto.copy(name = "Task 2"))
        val expectedTasks = taskDtoList.map { it.toTask() }

        // Mock the flow collection behavior
        coEvery { taskCollection.find<TaskDto>().collect(any()) } coAnswers {
            val collector = arg<FlowCollector<TaskDto>>(0)
            collector.emit(dto)
            collector.emit(dto.copy(name = "Task 2"))
        }

        // When
        val result = dataSource.getAllTasks()

        // Then
        assertEquals(2, result.size)
        assertEquals(expectedTasks[0].name, result[0].name)
        assertEquals(expectedTasks[1].name, result[1].name)
    }
}