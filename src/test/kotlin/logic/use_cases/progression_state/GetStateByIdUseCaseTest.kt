package logic.use_cases.progression_state

import io.mockk.every
import io.mockk.mockk
import logic.repositories.ProgressionStateRepository
import logic.entities.ProgressionState
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class GetStateByIdUseCaseTest {

 lateinit var getProgressionStateByIdUseCase: GetProgressionStateByIdUseCase
 val stateRepository: ProgressionStateRepository = mockk(relaxed = true)


 @BeforeEach
 fun setUp() {
  getProgressionStateByIdUseCase = GetProgressionStateByIdUseCase(stateRepository)
 }

 @Test
 fun `should throw IllegalArgumentException when state ID does not exist`() {
  // Given
  val stateId = "999"
  every { stateRepository.getStates() } returns Result.success(emptyList())

  // When & Then
  val exception = assertFailsWith<IllegalArgumentException> {
   getProgressionStateByIdUseCase.execute(stateId)
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
  val result = getProgressionStateByIdUseCase.execute(stateId).getOrThrow()

  // Then
  assertEquals(state, result)
 }


}