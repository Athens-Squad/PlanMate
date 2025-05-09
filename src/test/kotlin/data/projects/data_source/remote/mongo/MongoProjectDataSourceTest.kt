package data.projects.data_source.remote.mongo

import com.mongodb.client.model.Filters
import com.mongodb.kotlin.client.coroutine.MongoCollection
import data.progression_state.data_source.ProgressionStateDataSource
import data.tasks.data_source.TasksDataSource
import helper.project_helper.createProject
import helper.task_helper.FakeTask
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import data.projects.data_source.remote.mongo.dto.ProjectDto
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MongoProjectDataSourceTest {
    private val projectsCollection = mockk<MongoCollection<ProjectDto>>(relaxed = true)
    private val tasksDataSource = mockk<TasksDataSource>()
    private val statesDataSource = mockk<ProgressionStateDataSource>()
    private lateinit var dataSource: MongoProjectDataSource

    @BeforeEach
    fun setUp() {
        dataSource = MongoProjectDataSource(projectsCollection, tasksDataSource, statesDataSource)
    }

    @Test
    fun `should call insertOne when create project is called`() = runTest {
        // Given
        val project = createProject()
        val expectedDto = ProjectDto.fromProject(project)
        coEvery { projectsCollection.insertOne(expectedDto) } just Awaits

        // When
        dataSource.createProject(project)

        // Then
        coVerify(exactly = 1) { projectsCollection.insertOne(expectedDto) }
    }

    @Test
    fun `Given a project When updateProject is called Then replaceOne is called with correct DTO`() = runTest {
        // Given
        val project = createProject()
        val expectedDto = ProjectDto.fromProject(project)
        coEvery { projectsCollection.replaceOne(any(), expectedDto) } returns mockk()

        // When
        dataSource.updateProject(project)

        // Then
        coVerify(exactly = 1) {
            projectsCollection.replaceOne(Filters.eq("_id", project.id), expectedDto)
        }
    }

    @Test
    fun `Given a project ID When deleteProject is called Then deleteOne is called with correct filter`() = runTest {
        // Given
        val projectId = "project1"
        coEvery { projectsCollection.deleteOne(any()) } returns mockk()

        // When
        dataSource.deleteProject(projectId)

        // Then
        coVerify(exactly = 1) {
            projectsCollection.deleteOne(Filters.eq("_id", projectId))
        }
    }

    @Test
    fun `Given a project with tasks When getProjects is called Then it returns the project with those tasks`() = runTest {
        // Given
        val projectDto = ProjectDto.fromProject(createProject(id = "p1"))
        val expectedTask = FakeTask.fakeTask.copy(projectId = "p1")
        coEvery { projectsCollection.find().map { Any() }.toList() } returns listOf(projectDto)
        coEvery { tasksDataSource.getTasksByProjectId("p1") } returns listOf(expectedTask)
        coEvery { statesDataSource.getProgressionStates() } returns emptyList()

        // When
        val result = dataSource.getProjects()

        // Then
        assertEquals(listOf(expectedTask), result[0].tasks)
    }


}