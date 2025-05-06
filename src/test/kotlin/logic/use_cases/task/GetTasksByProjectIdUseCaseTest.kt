package logic.use_cases.task

import com.google.common.truth.Truth.assertThat
import helper.task_helper.FakeTask.fakeTask
import io.mockk.every
import io.mockk.mockk
import logic.repositories.TasksRepository

import kotlin.test.BeforeTest
import kotlin.test.Test

class GetTasksByProjectIdUseCaseTest {
 private val tasksRepository = mockk<TasksRepository>()
 private lateinit var getTasksByProjectIdUseCase: GetTasksByProjectIdUseCase


 @BeforeTest
 fun setup() {
  getTasksByProjectIdUseCase = GetTasksByProjectIdUseCase(tasksRepository)
 }

 @Test
 fun `should return tasks for valid project ID`() {
  val tasks = listOf(fakeTask, fakeTask.copy(title = "task 2"))
  every { tasksRepository.getTasksByProjectId(fakeTask.projectId) } returns Result.success(tasks)

  val result = getTasksByProjectIdUseCase.execute(fakeTask.projectId)

  assertThat(result.isSuccess).isTrue()
  assertThat(result.getOrNull()).isEqualTo(tasks)
 }

 @Test
 fun `should return failure for invalid project ID`() {
  val projectNotFoundException = CannotCompleteTaskOperationException("Project not found")
  every { tasksRepository.getTasksByProjectId(fakeTask.projectId) } returns Result.failure(projectNotFoundException)

  val result = getTasksByProjectIdUseCase.execute(fakeTask.projectId)

  assertThat(result.isFailure).isTrue()
  assertThat(result.exceptionOrNull()).isEqualTo(projectNotFoundException)
 }
}