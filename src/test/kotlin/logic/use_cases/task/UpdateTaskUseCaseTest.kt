package logic.use_cases.task

import com.google.common.truth.Truth.assertThat
import helper.task_helper.FakeTask
import io.mockk.every
import io.mockk.mockk
import logic.repositories.AuditRepository
import logic.repositories.ProjectsRepository
import logic.repositories.TasksRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class UpdateTaskUseCaseTest {
    private lateinit var updateTaskUseCase: UpdateTaskUseCase
    private val projectsRepository = mockk<ProjectsRepository>()
    private val tasksRepository = mockk<TasksRepository>()
    private val auditRepository = mockk<AuditRepository>()

    @BeforeEach
    fun setup() {
        updateTaskUseCase = UpdateTaskUseCase(tasksRepository, projectsRepository, auditRepository)
    }

    @Test
    fun `should be able to update task if the task already exists then return true`() {
        every { tasksRepository.updateTask(FakeTask.fakeTask) } returns Result.success(Unit)

        //when
        val result = updateTaskUseCase.execute(FakeTask.fakeTask)

        //then
    }

    @Test
    fun `should not be able to update a task when task does not exist then return false`() {
        every { tasksRepository.updateTask(FakeTask.fakeTask) } returns Result.failure(NoSuchElementException())

        //when
        val result = updateTaskUseCase.execute(FakeTask.fakeTask)

        //then
    }

    @Test
    fun `should not be able to change projectId then return false`() {

    }

    @Test
    fun `should not be able to change the taskName to existing taskName then return false` () {

    }
}