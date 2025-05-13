package logic.use_cases.task

import com.google.common.truth.Truth.assertThat
import helper.task_helper.FakeTask
import helper.task_helper.FakeTask.fakeTask
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import logic.exceptions.NoProjectFoundForTaskException
import logic.repositories.TasksRepository
import logic.use_cases.task.taskvalidations.TaskValidator
import org.junit.jupiter.api.assertThrows

import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.uuid.ExperimentalUuidApi

class GetTasksByProjectIdUseCaseTest {
    private val tasksRepository: TasksRepository = mockk(relaxed = true)
    private val taskValidator: TaskValidator = mockk(relaxed = true)
    private lateinit var getTasksByProjectIdUseCase: GetTasksByProjectIdUseCase


    @BeforeTest
    fun setup() {
        getTasksByProjectIdUseCase = GetTasksByProjectIdUseCase(tasksRepository, taskValidator)
    }

    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun `should return tasks for valid project ID`() {
        runTest {
            //given
            val dummyTask = FakeTask.fakeTask
            coEvery { taskValidator.validateProjectExists(dummyTask.projectId) } returns true
            //when
            getTasksByProjectIdUseCase.execute(dummyTask.projectId)
            //then
            coVerify { tasksRepository.getTasksByProjectId(dummyTask.projectId) }
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun `should not return tasks for invalid project ID`() {
        runTest {
            val dummyTask = FakeTask.fakeTask
            coEvery { taskValidator.validateProjectExists(dummyTask.projectId) } throws NoProjectFoundForTaskException()
            //when & then
            assertThrows<NoProjectFoundForTaskException> {
                getTasksByProjectIdUseCase.execute(dummyTask.projectId)
            }
        }
    }

}
