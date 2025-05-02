package logic.use_cases.state

import helper.task_helper.createDummyState
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import logic.repositories.StatesRepository
import logic.entities.ProgressionState
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class GetStateByIdUseCaseTest {

 lateinit var getStateByIdUseCase: GetStateByIdUseCase
 val stateRepository: StatesRepository = mockk(relaxed = true)


 @BeforeEach
 fun setUp() {
  getStateByIdUseCase = GetStateByIdUseCase(stateRepository)
 }

 @Test
 fun `should throw IllegalArgumentException when state ID does not exist`() {
  // Given
  val stateId = "999"
  every { stateRepository.getStates() } returns Result.success(emptyList())

  // When & Then
  val exception = assertFailsWith<IllegalArgumentException> {
   getStateByIdUseCase.execute(stateId)
  }

  assertEquals("Project with ID $stateId does not exist.", exception.message)
 }

 @Test
 fun `should return state when state ID exists`() {
  // Given
  val stateId = "123"
  val state = ProgressionState(id = stateId, name = "Test State" , projectId = "44")
  every { stateRepository.getStates() } returns Result.success(listOf(state))

  // When
  val result = getStateByIdUseCase.execute(stateId)

  // Then
  assertEquals(state, result)
 }


}