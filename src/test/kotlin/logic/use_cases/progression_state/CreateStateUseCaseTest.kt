@file:OptIn(ExperimentalUuidApi::class)

package logic.use_cases.progression_state


import helper.progression_state_helper.createDummyState
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import logic.exceptions.ProgressionStateAlreadyExistsException
import logic.repositories.ProgressionStateRepository
import net.thechance.logic.use_cases.progression_state.progressionStateValidations.ProgressionStateValidator
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.uuid.ExperimentalUuidApi

class CreateStateUseCaseTest {

    lateinit var createProgressionStateUseCase: CreateProgressionStateUseCase
    val stateRepository: ProgressionStateRepository = mockk(relaxed = true)
    var progressionStateValidator: ProgressionStateValidator = mockk(relaxed = true)


    @BeforeEach
    fun setUp() {
        createProgressionStateUseCase = CreateProgressionStateUseCase(stateRepository, progressionStateValidator)
    }


    @Test
    fun `should call repository when progression state is created`() {
        runTest {
            //given
            val progressionState = createDummyState.dummyState()
            //when
            coEvery { progressionStateValidator.validateProjectExists(progressionState.projectId) } returns true
            coEvery { progressionStateValidator.validateProgressionStateNotExists(progressionState.id) } returns true
            coEvery { progressionStateValidator.validateProgressionStateFieldsNotBlank(progressionState) } returns true

            //then
            createProgressionStateUseCase.execute(progressionState)
            coVerify(exactly = 1) { stateRepository.createProgressionState(progressionState) }
        }
    }


    @Test
    fun `should throw exception when progression state is already exist `() {
        runTest {
            // Given
            val progressionState = createDummyState.dummyState()

            coEvery { progressionStateValidator.validateProgressionStateNotExists(progressionState.id) } throws
                    ProgressionStateAlreadyExistsException()

            // When & Then
            assertThrows<ProgressionStateAlreadyExistsException> {
                createProgressionStateUseCase.execute(progressionState)
            }
        }


    }
}
