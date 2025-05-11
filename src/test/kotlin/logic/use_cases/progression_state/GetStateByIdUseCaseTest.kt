package logic.use_cases.progression_state

import com.google.common.truth.Truth.assertThat
import helper.progression_state_helper.createDummyState
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import logic.repositories.ProgressionStateRepository
import logic.entities.ProgressionState
import net.thechance.data.progression_state.exceptions.ProgressionStateNotFoundException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
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
    fun `should return progression state when found`() {
        runTest {
            // given
            val progressionStateId = createDummyState.dummyState().id

            val expectedProgressionState = createDummyState.dummyState()

            coEvery { stateRepository.getProgressionStates() } returns listOf (expectedProgressionState)

            // when
            val result = getProgressionStateByIdUseCase.execute(progressionStateId)

            // then
            assertThat(result).isEqualTo(expectedProgressionState)
        }
    }

    @Test
    fun `should throw Progression State Not Found Exception when not found`()
    {
        runTest {
            // given
            val progressionStateId = createDummyState.dummyState().id

            coEvery { stateRepository.getProgressionStates() } returns emptyList() // No progression states available

            // when & then
            assertThrows<ProgressionStateNotFoundException> {
                getProgressionStateByIdUseCase.execute(progressionStateId)
            }
        }
    }


}