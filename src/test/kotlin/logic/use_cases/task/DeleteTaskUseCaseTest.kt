package logic.use_cases.task

import com.google.common.truth.Truth.assertThat
import helper.task_helper.FakeTask
import helper.task_helper.FakeTask.fakeTask
import helper.task_helper.FakeTask.fakeUserName
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import logic.entities.EntityType
import logic.entities.Task
import logic.exceptions.TaskNotFoundException
import logic.repositories.AuditRepository
import logic.repositories.TasksRepository
import logic.use_cases.audit_log.CreateAuditLogUseCase

import net.thechance.logic.validators.taskvalidations.TaskValidator
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.uuid.ExperimentalUuidApi

class DeleteTaskUseCaseTest {
    private lateinit var deleteTaskUseCase: DeleteTaskUseCase
    private val fakeTaskValidator : TaskValidator = mockk(relaxed = true)
    val fakeTasksRepository : TasksRepository = mockk(relaxed = true)
    private val fakeAuditLogUseCase : CreateAuditLogUseCase = mockk(relaxed = true)

    @BeforeEach
    fun setup() {
        deleteTaskUseCase = DeleteTaskUseCase(fakeTasksRepository,fakeAuditLogUseCase,fakeTaskValidator)
    }
    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun`should delete task if task already exist`(){
        runTest {
            //given
            val dummyTask = FakeTask.fakeTask
            val dummyUserName = FakeTask.fakeUserName

            coEvery { fakeTaskValidator.validateTaskAlreadyExists(dummyTask.id) } returns true

            //when
            deleteTaskUseCase.execute(dummyTask.id ,dummyUserName)

            //then
            coVerify { fakeTasksRepository.deleteTask(dummyTask.id) }
        }
    }
    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun`should not delete task if task already exist`(){
        runTest {
            //given
            val dummyTask = FakeTask.fakeTask
            val dummyUserName = FakeTask.fakeUserName

            coEvery { fakeTaskValidator.validateTaskAlreadyExists(dummyTask.id) } throws TaskNotFoundException()

            //when & then
            assertThrows<TaskNotFoundException>() {
                deleteTaskUseCase.execute(dummyTask.id, dummyUserName)
            }

        }
    }
    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun`should call AuditLog when task is delete`(){
        runTest {
            //given
            val dummyTask = FakeTask.fakeTask
            val dummyUserName = FakeTask.fakeUserName
            //when
            deleteTaskUseCase.execute(dummyTask.id , dummyUserName)

            //then
            fakeTasksRepository.deleteTask(dummyTask.id)
            coVerify(exactly = 1){ fakeAuditLogUseCase.execute(
                withArg {
                    assert(it.entityType == EntityType.TASK)
                    assert(it.description == "Task deleted successfully.")
                    assert(it.entityId == dummyTask.id)
                    assert(it.userName == dummyUserName)
                }
            )}

        }
    }
}