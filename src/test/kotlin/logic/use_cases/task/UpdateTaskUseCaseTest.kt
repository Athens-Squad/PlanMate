package logic.use_cases.task

import com.google.common.truth.Truth.assertThat
import helper.task_helper.FakeTask.fakeTask
import helper.task_helper.FakeTask.fakeUserName
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import logic.entities.Task
import logic.repositories.AuditRepository
import logic.repositories.TasksRepository
import logic.exceptions.TasksException
import logic.use_cases.task.taskvalidations.TaskValidator
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class UpdateTaskUseCaseTest {
    private lateinit var updateTaskUseCase: UpdateTaskUseCase
    private val taskValidator = mockk<TaskValidator>()
    private val tasksRepository = mockk<TasksRepository>()
    private val auditRepository = mockk<AuditRepository>()

    @BeforeEach
    fun setup() {
        updateTaskUseCase = UpdateTaskUseCase(tasksRepository, auditRepository, taskValidator)
    }

    @Test
    fun `should be able to update task if the task already exists`() {
        // Given
        val fakeUpdatedTask = fakeTask.copy(
            title = "newTitle"
        )

        every { taskValidator.doIfTaskExistsOrThrow(any(), any()) } answers {
            val action = secondArg<(Task) -> Unit>()
            action(fakeTask)
        }
        every { taskValidator.validateTaskBeforeUpdating(fakeTask, fakeUpdatedTask) } just Runs
        every { tasksRepository.updateTask(fakeUpdatedTask) } returns Result.success(Unit)
        every { auditRepository.createAuditLog(any()) } returns Result.success(Unit)

        // When
        val result = updateTaskUseCase.execute(fakeUpdatedTask, fakeUserName)

        if (result.isFailure) {
            println(result.exceptionOrNull())
        }
        // Then
        assertThat(result.isSuccess).isTrue()
    }

    @Test
    fun `should not be able to update a task when task does not exist then return false`() {
        //given
        val fakeUpdatedTask = fakeTask.copy(title = "newTitle")
        every { taskValidator.doIfTaskExistsOrThrow(fakeUpdatedTask.id, any()) } throws
                TasksException.CannotCompleteTaskOperationException("Cannot find the task!")

        every { taskValidator.validateTaskBeforeUpdating(fakeTask, fakeUpdatedTask) } just Runs
        every { tasksRepository.updateTask(fakeUpdatedTask) } returns Result.success(Unit)
        every { auditRepository.createAuditLog(any()) } returns Result.success(Unit)

        // When
        val result = updateTaskUseCase.execute(fakeUpdatedTask, fakeUserName)

        // Then
        assertThat(result.isFailure).isTrue()
    }

    @Test
    fun `should not be able to change projectId`() {
    }

    @Test
    fun `should not be able to change the taskTitle to existing taskTitle` () {

    }
}