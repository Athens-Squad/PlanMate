package logic.use_cases.task

import com.google.common.truth.Truth.assertThat
import helper.task_helper.FakeTask
import helper.task_helper.FakeTask.fakeTask
import helper.task_helper.FakeTask.fakeUserName
import io.mockk.every
import io.mockk.mockk
import logic.entities.Task
import logic.repositories.AuditRepository
import logic.repositories.TasksRepository
import net.thechance.logic.exceptions.TasksException
import net.thechance.logic.use_cases.task.taskvalidations.TaskValidator
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class DeleteTaskUseCaseTest {
    private lateinit var deleteTaskUseCase: DeleteTaskUseCase
    private val taskValidator = mockk<TaskValidator>()
    private val tasksRepository = mockk<TasksRepository>()
    private val auditRepository = mockk<AuditRepository>()

    @BeforeEach
    fun setup() {
        deleteTaskUseCase = DeleteTaskUseCase(tasksRepository, auditRepository, taskValidator)
    }

    @Test
    fun `should delete task when it exists`() {
        //given
        every { taskValidator.doIfTaskExistsOrThrow(fakeTask.id, any()) } answers {
            secondArg<() -> Unit>().invoke()
        }
        every { tasksRepository.deleteTask(fakeTask.id) } returns Result.success(Unit)
        every { auditRepository.createAuditLog(any()) } returns Result.success(Unit)

        //when
        val result = deleteTaskUseCase.execute(fakeTask.id, fakeUserName)

        //then
        assertThat(result.isSuccess).isTrue()
    }

    @Test
    fun `should not be able to delete when task does not exist then return false`() {
        //given
        every { taskValidator.doIfTaskExistsOrThrow(fakeTask.id, any()) } throws
                TasksException.CannotCompleteTaskOperationException("Cannot find the task!")

        every { tasksRepository.deleteTask(fakeTask.id) } returns Result.success(Unit)
        every { auditRepository.createAuditLog(any()) } returns Result.success(Unit)

        //when
        val result = deleteTaskUseCase.execute(fakeTask.id, fakeUserName)

        //then
        assertThat(result.isFailure).isTrue()
    }
}