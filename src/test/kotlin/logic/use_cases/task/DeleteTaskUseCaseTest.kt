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

class DeleteTaskUseCaseTest {
    private lateinit var deleteTaskUseCase: DeleteTaskUseCase
    private val tasksRepository = mockk<TasksRepository>()
    private val auditRepository = mockk<AuditRepository>()

    @BeforeEach
    fun setup() {
        deleteTaskUseCase = DeleteTaskUseCase(tasksRepository, auditRepository)
    }

    @Test
    fun `should delete task when it exists and return false`() {
        every { tasksRepository.deleteTask(FakeTask.fakeTask.id) } returns Result.success(Unit)

        //when
        val result = deleteTaskUseCase.execute(FakeTask.fakeTask.id)

        //then
        assertThat(result).isTrue()
    }

    @Test
    fun `should not be able to delete when task does not exist then return false`() {
        every { tasksRepository.deleteTask(FakeTask.fakeTask.id) } returns Result.failure(NoSuchElementException())

        //when
        val result = deleteTaskUseCase.execute(FakeTask.fakeTask.id)

        //then
        assertThat(result).isFalse()
    }
}