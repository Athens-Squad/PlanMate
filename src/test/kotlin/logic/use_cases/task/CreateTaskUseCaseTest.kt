package logic.use_cases.task

import com.google.common.truth.Truth.*
import helper.task_helper.FakeTask.fakeAuditLog
import helper.task_helper.FakeTask.fakeTask
import helper.task_helper.FakeTask.fakeUserName
import io.mockk.*
import kotlinx.coroutines.test.runTest
import logic.exceptions.*
import logic.repositories.TasksRepository
import logic.use_cases.audit_log.CreateAuditLogUseCase

import logic.use_cases.task.taskvalidations.TaskValidator
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CreateTaskUseCaseTest {
    private lateinit var createTaskUseCase: CreateTaskUseCase
    private val fakeTaskValidator = mockk<TaskValidator>()
    private val fakeTasksRepository = mockk<TasksRepository>()
    private val fakCreateAuditLogUseCase = mockk<CreateAuditLogUseCase>()

    @BeforeEach
    fun setup() {
        createTaskUseCase = CreateTaskUseCase(fakeTasksRepository, fakCreateAuditLogUseCase, fakeTaskValidator)
    }

    @Test
    fun `should create Task when Task is valid`() {
        runTest {
            //given
            fakeTask
            //when
            coEvery { fakeTaskValidator.validateTaskBeforeCreation(fakeTask) } returns true

            createTaskUseCase.execute(fakeTask , fakeUserName)
            //then
            coVerify{(fakeTasksRepository.createTask(fakeTask))}
        }

    }
}

   /*
    @Test
    fun `should create auditLog when Task created successfully`() {
        //given
        every { fakeTaskValidator.doIfTaskNotExistsOrThrow(fakeTask, any()) } answers {
            secondArg<() -> Unit>().invoke()
        }
        every { fakeTaskValidator.validateTaskBeforeCreation(fakeTask) } just Runs
        every { fakeTasksRepository.createTask(fakeTask) } returns Result.success(Unit)
        every { auditRepository.createAuditLog(any()) } returns Result.success(Unit)
        every { createTaskUseCase.execute(fakeTask, fakeUserName) } returns Result.success(Unit)

        // When
        val result = auditRepository.createAuditLog(fakeAuditLog)

        // Then
        assertThat(result.isSuccess).isTrue()

    }

    @Test
    fun `should not create auditLog when Task didn't created successfully`() {
        //given
        every { fakeTaskValidator.doIfTaskNotExistsOrThrow(fakeTask, any()) } answers {
            secondArg<() -> Unit>().invoke()
        }
        every { fakeTaskValidator.validateTaskBeforeCreation(fakeTask) } just Runs
        every { fakeTasksRepository.createTask(fakeTask) } returns Result.success(Unit)
        every { createTaskUseCase.execute(fakeTask, fakeUserName) } returns Result.success(Unit)
        every { auditRepository.createAuditLog(any()) } returns Result.failure(Exception())

        // When
        val result = auditRepository.createAuditLog(fakeAuditLog)

        // Then
        assertThat(result.isFailure).isTrue()

    }

    @Test
    fun `should not create Task when there is already Task with the same id`() {
        every { fakeTaskValidator.doIfTaskNotExistsOrThrow(fakeTask, any()) } throws
                CannotCompleteTaskOperationException("There is existing fakeTask with same id")

        // When
        val result = createTaskUseCase.execute(fakeTask, fakeUserName)

        // Then
        assertThat(result.isFailure).isTrue()
    }


    @Test
    fun `should not be able to create Task with non existing projectId`() {
        every { fakeTaskValidator.doIfTaskNotExistsOrThrow(fakeTask, any()) } throws
                InvalidTaskException("Project with ID ${fakeTask.projectId} does not exist.")

        // When
        val result = createTaskUseCase.execute(fakeTask, fakeUserName)

        // Then
        assertThat(result.isFailure).isTrue()
    }

    @Test
    fun `should not be able to create Task with the same title within the same projectId`() {
        every { fakeTaskValidator.doIfTaskNotExistsOrThrow(fakeTask, any()) } throws
                InvalidTaskException("Task with the same title already exist in this project.")

        // When
        val result = createTaskUseCase.execute(fakeTask, fakeUserName)

        // Then
        assertThat(result.isFailure).isTrue()
    }

}

    */