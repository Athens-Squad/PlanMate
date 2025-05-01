package logic.use_cases.task

import com.google.common.truth.Truth.assertThat
import helper.task_helper.FakeTask.fakeTask
import io.mockk.every
import io.mockk.mockk
import logic.repositories.TasksRepository
import kotlin.test.BeforeTest
import kotlin.test.Test


class GetTaskByIdUseCaseTest {
 private val tasksRepository = mockk<TasksRepository>()
 private lateinit var getTaskByIdUseCase: GetTaskByIdUseCase



 @BeforeTest
 fun setup() {
  getTaskByIdUseCase = GetTaskByIdUseCase(tasksRepository)
 }

 @Test
 fun `should return task if it exists`() {
  every { tasksRepository.getTaskById(fakeTask.id) } returns Result.success(fakeTask)

  val result = getTaskByIdUseCase.execute(fakeTask.id)

  assertThat(result.isSuccess).isTrue()
  assertThat(result.getOrNull()).isEqualTo(fakeTask)
 }

 @Test
 fun `should return failure if task is not found`() {
  //given
  val taskNotFoundException = NoSuchElementException("No such Task with taskId: ${fakeTask.id}")

  every { tasksRepository.getTaskById(fakeTask.id) } returns
          Result.failure(taskNotFoundException)

  val result = getTaskByIdUseCase.execute(fakeTask.id)

  assertThat(result.isFailure).isTrue()
  assertThat(result.exceptionOrNull()).isEqualTo(taskNotFoundException)
 }
}