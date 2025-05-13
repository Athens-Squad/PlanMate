package logic.use_cases.task

import com.google.common.truth.Truth.assertThat
import helper.task_helper.FakeTask
import helper.task_helper.FakeTask.fakeTask
import helper.task_helper.FakeTask.fakeUserName
import io.mockk.*
import kotlinx.coroutines.test.runTest
import logic.entities.EntityType
import logic.entities.Task
import logic.exceptions.InvalidTaskFieldsException
import logic.exceptions.NoProjectFoundForTaskException
import logic.exceptions.TaskAlreadyExistsException
import logic.repositories.AuditRepository
import logic.repositories.TasksRepository
import logic.use_cases.audit_log.CreateAuditLogUseCase

import net.thechance.logic.validators.taskvalidations.TaskValidator
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.uuid.ExperimentalUuidApi

class UpdateTaskUseCaseTest {
    private lateinit var updateTaskUseCase: UpdateTaskUseCase
    private val taskValidator: TaskValidator = mockk(relaxed = true)
    private val tasksRepository: TasksRepository = mockk(relaxed = true)
    private val createAuditLogUseCase: CreateAuditLogUseCase = mockk(relaxed = true)

    @BeforeEach
    fun setup() {
        updateTaskUseCase = UpdateTaskUseCase(tasksRepository, createAuditLogUseCase, taskValidator)
    }

    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun `should be able to update task if the task already exists`() {
        runTest {
            //given
            val dummyTask = FakeTask.fakeTask
            val dummyUserName = FakeTask.fakeUserName

            coEvery { taskValidator.validateTaskFieldsNotBlank(dummyTask) } returns true
            coEvery { taskValidator.validateTaskNotExists(dummyTask.id) } returns true
            coEvery { taskValidator.validateProgressionStateExists(dummyTask.currentProgressionState.id) } returns true
            coEvery { taskValidator.validateProjectExists(dummyTask.projectId) } returns true

            //when
            updateTaskUseCase.execute(dummyTask, dummyUserName)

            //then
            coVerify { tasksRepository.updateTask(dummyTask) }

        }

    }

    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun `should not update task if task field is blank `() {
        runTest {
            //given
            val dummyTask = FakeTask.fakeTask
            val dummyUserName = FakeTask.fakeUserName

            coEvery { taskValidator.validateTaskFieldsNotBlank(dummyTask) } throws InvalidTaskFieldsException()

            //when & then

            assertThrows<InvalidTaskFieldsException> {
                updateTaskUseCase.execute(dummyTask, dummyUserName)
            }

        }
    }

    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun `should not update task when project not exist`() {
        runTest {
            //given
            val dummyTask = FakeTask.fakeTask
            val dummyUserName = FakeTask.fakeUserName

            coEvery { taskValidator.validateProjectExists(dummyTask.projectId) } throws NoProjectFoundForTaskException()

            //when & then
            assertThrows<NoProjectFoundForTaskException> {
                updateTaskUseCase.execute(dummyTask, dummyUserName)
            }
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun `should create AuditLog when update task`() {
        runTest {
            //given
            val dummyTask = FakeTask.fakeTask
            val dummyAuditLog = FakeTask.fakeAuditLog
            val dummyUserName = FakeTask.fakeUserName

            //when
            updateTaskUseCase.execute(dummyTask, dummyUserName)
            //then
            coVerify {
                createAuditLogUseCase.execute(
                    withArg {
                        assert(it.entityType == EntityType.TASK)
                        assert(it.description == "Project updated successfully.")
                        assert(it.entityId == dummyTask.id)
                        assert(it.userName == dummyUserName)
                    }
                )
            }
        }
    }
}

