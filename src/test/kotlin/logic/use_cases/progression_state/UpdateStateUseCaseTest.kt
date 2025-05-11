package logic.use_cases.progression_state

import helper.progression_state_helper.createDummyState
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import logic.repositories.ProgressionStateRepository
import net.thechance.data.progression_state.exceptions.ProgressionStateNotFoundException
import net.thechance.logic.use_cases.progression_state.progressionStateValidations.ProgressionStateValidator
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class UpdateStateUseCaseTest {
    lateinit var updateState: UpdateProgressionStateUseCase
    val stateRepository: ProgressionStateRepository = mockk(relaxed = true)
    var progressionStateValidator: ProgressionStateValidator = mockk(relaxed = true)


    @BeforeEach
    fun setUp() {
        updateState = UpdateProgressionStateUseCase(stateRepository, progressionStateValidator)
    }

    @Test
    fun `should throw exception when validation fails`() {
        runTest {
            // given
            val UpdateProgressionState = createDummyState.dummyState()
            coEvery { progressionStateValidator.validateAfterCreation(UpdateProgressionState.id) } returns ProgressionStateNotFoundException()

            // when & then
            assertThrows<ProgressionStateNotFoundException> {
                updateState.execute(UpdateProgressionState)
            }

            coVerify(exactly = 0) { stateRepository.updateProgressionState(any()) }
        }
    }

    @Test
    fun `should update progression state when validation succeeds`()
    {
        runTest {
            // given
            val updatedProgressionState = createDummyState.dummyState()

            coEvery { progressionStateValidator.validateAfterCreation(updatedProgressionState.id) } returns null // Validation succeeds

            // when
            updateState.execute(updatedProgressionState)

            // then
            coVerify { stateRepository.updateProgressionState(updatedProgressionState) }
        }
    }

}