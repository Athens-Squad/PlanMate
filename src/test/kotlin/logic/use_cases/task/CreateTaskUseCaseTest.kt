package logic.use_cases.task

import helper.task_helper.FakeTask
import io.mockk.*
import kotlinx.coroutines.test.runTest
import logic.entities.EntityType
import logic.exceptions.*
import logic.repositories.TasksRepository
import logic.use_cases.audit_log.CreateAuditLogUseCase
import logic.use_cases.task.taskvalidations.TaskValidator
import logic.use_cases.task.taskvalidations.TaskValidatorImpl
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDateTime
import kotlin.uuid.ExperimentalUuidApi

class CreateTaskUseCaseTest {
    private lateinit var createTaskUseCase: CreateTaskUseCase
    private val fakeTaskValidator: TaskValidatorImpl = mockk(relaxed = true)
    private val fakeTasksRepository: TasksRepository = mockk(relaxed = true)
    private val fakCreateAuditLogUseCase: CreateAuditLogUseCase = mockk(relaxed = true)

    @BeforeEach
    fun setup() {
        createTaskUseCase = CreateTaskUseCase(fakeTasksRepository, fakCreateAuditLogUseCase, fakeTaskValidator)
    }

    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun `should create Task when Task is valid`() {
        runTest {
            //given
            val dummyTask = FakeTask.fakeTask
            val dummyUserName = FakeTask.fakeUserName


            coEvery { fakeTaskValidator.validateTaskFieldsNotBlank(dummyTask) } returns true
            coEvery { fakeTaskValidator.validateTaskNotExists(dummyTask.id) } returns true
            coEvery { fakeTaskValidator.validateProgressionStateExists(dummyTask.currentProgressionState.id) } returns true
            coEvery { fakeTaskValidator.validateProjectExists(dummyTask.projectId) } returns true

            createTaskUseCase.execute(dummyTask, dummyUserName)
            //then
            coVerify { (fakeTasksRepository.createTask(dummyTask)) }
        }

    }

    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun `should not create task if it is already exist`() {
        runTest {
            //given
            val dummyTask = FakeTask.fakeTask
            val dummyUserName = FakeTask.fakeUserName

            coEvery { fakeTaskValidator.validateTaskNotExists(dummyTask.id) } throws TaskAlreadyExistsException()

            //when & then
            assertThrows<TaskAlreadyExistsException> {
                createTaskUseCase.execute(dummyTask, dummyUserName)
            }
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun `should not create task if project no found `() {
        runTest {
            //given
            val dummyTask = FakeTask.fakeTask
            val dummyUserName = FakeTask.fakeUserName


            coEvery { fakeTaskValidator.validateTaskFieldsNotBlank(dummyTask) } returns true
            coEvery { fakeTaskValidator.validateTaskNotExists(dummyTask.id) } returns true
            coEvery { fakeTaskValidator.validateProgressionStateExists(dummyTask.currentProgressionState.id) } returns true
            coEvery { fakeTaskValidator.validateProjectExists(dummyTask.id) } throws NoProjectFoundForTaskException()
            coEvery { fakeTasksRepository.createTask(dummyTask) } just runs

            //when & then
            assertThrows<NoProjectFoundForTaskException> {
                createTaskUseCase.execute(dummyTask ,dummyUserName)
            }
        }
    }

    @Test
    fun `should not create task if fields is blank`() {
        runTest {
            //given
            val dummyTask = FakeTask.fakeTask
            val dummyUserName = FakeTask.fakeUserName

            coEvery { fakeTaskValidator.validateTaskFieldsNotBlank(dummyTask) } throws InvalidTaskFieldsException()

            //when & then
            assertThrows<InvalidTaskFieldsException> {
                createTaskUseCase.execute(dummyTask, dummyUserName)
            }
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun `should not create task if no valid progressionState `() {
        runTest {
            //given
            val dummyTask = FakeTask.fakeTask
            val dummyUserName = FakeTask.fakeUserName

            coEvery { fakeTaskValidator.validateProgressionStateExists(dummyTask.currentProgressionState.id) } throws NoProgressionStateFoundForTaskException()

            //when & then
            assertThrows<NoProgressionStateFoundForTaskException> {
                createTaskUseCase.execute(dummyTask, dummyUserName)
            }
        }
    }
    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun`should call AuditLog if when task creation`(){
        runTest {
            //given
            val dummyTask = FakeTask.fakeTask
            val dummyUserName = FakeTask.fakeUserName
            val dummyAuditLog = FakeTask.fakeAuditLog
            //when
            coEvery { fakeTaskValidator.validateTaskFieldsNotBlank(dummyTask) } returns true
            coEvery { fakeTaskValidator.validateTaskNotExists(dummyTask.id) } returns true
            coEvery { fakeTaskValidator.validateProgressionStateExists(dummyTask.currentProgressionState.id) } returns true
            coEvery { fakeTaskValidator.validateProjectExists(dummyTask.projectId) } returns true
            coEvery { fakeTasksRepository.createTask(dummyTask) } just runs

            createTaskUseCase.execute(dummyTask ,dummyUserName)
            //then
            coVerify { fakCreateAuditLogUseCase.execute(
                withArg {
                    assert(it.entityType == EntityType.TASK)
                    assert(it.description == "Task created successfully." )
                    assert(it.entityId == dummyTask.id)
                    assert(it.userName == dummyUserName)

                }
            ) }


        }
    }


}


