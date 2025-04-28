package logic.use_cases.task

import com.google.common.truth.Truth.*
import helper.task_helper.FakeTask
import io.mockk.every
import io.mockk.mockk
import logic.repositories.AuditRepository
import logic.repositories.ProjectsRepository
import logic.repositories.TasksRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CreateTaskUseCaseTest {
    private lateinit var createTaskUseCase: CreateTaskUseCase
    private val projectsRepository = mockk<ProjectsRepository>()
    private val tasksRepository = mockk<TasksRepository>()
    private val auditRepository = mockk<AuditRepository>()

    @BeforeEach
    fun setup() {
        createTaskUseCase = CreateTaskUseCase(tasksRepository, projectsRepository, auditRepository)
    }

    @Test
    fun `should create task when task is valid then return true`() {
        every { tasksRepository.createTask(FakeTask.fakeTask) } returns true

        //when
        val result = createTaskUseCase.execute(FakeTask.fakeTask)

        //then
        assertThat(result).isTrue()
    }

    @Test
    fun `should not create task when there is already task with the same id`() {
        every { tasksRepository.createTask(FakeTask.fakeTask) } returns false

        //when
        val result = createTaskUseCase.execute(FakeTask.fakeTask)

        //then
        assertThat(result).isFalse()
    }

    @Test
    fun `should be able to create task with valid taskId` () {

    }

    @Test
    fun `should not be able to create task with invalid taskId`() {

    }

    @Test
    fun `should be able to create task with existing projectId` () {

    }

    @Test
    fun `should not be able to create task with non existing projectId`() {

    }

    @Test
    fun `should not be able to create task with the same name within the same projectId`() {

    }

}