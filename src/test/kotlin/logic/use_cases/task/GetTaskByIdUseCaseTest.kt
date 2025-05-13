package logic.use_cases.task

import com.google.common.truth.Truth.assertThat
import helper.task_helper.FakeTask
import helper.task_helper.FakeTask.fakeTask
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import logic.exceptions.TaskNotFoundException
import logic.repositories.TasksRepository
import net.thechance.logic.validators.taskvalidations.TaskValidator
import org.junit.jupiter.api.assertThrows
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.uuid.ExperimentalUuidApi


class GetTaskByIdUseCaseTest {
    private val tasksRepository: TasksRepository = mockk(relaxed = true)
    private val taskValidator: TaskValidator = mockk(relaxed = true)
    private lateinit var getTaskByIdUseCase: GetTaskByIdUseCase


    @BeforeTest
    fun setup() {
        getTaskByIdUseCase = GetTaskByIdUseCase(tasksRepository, taskValidator)
    }

    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun `should return task if it exists`() {
        runTest {
            //given
            val dummyTask = FakeTask.fakeTask
            coEvery { taskValidator.validateTaskAlreadyExists(dummyTask.id) } returns true
            //when
            getTaskByIdUseCase.execute(dummyTask.id)
            //then
            coVerify { tasksRepository.getTaskById(dummyTask.id) }

        }

    }
    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun`should not return task if it not exist`(){
        runTest {
            //given
            val dummyTask = FakeTask.fakeTask

            coEvery { taskValidator.validateTaskAlreadyExists(dummyTask.id) } throws  TaskNotFoundException()
            //when & then
            assertThrows<TaskNotFoundException> {
                getTaskByIdUseCase.execute(dummyTask.id)
            }


        }
    }

}