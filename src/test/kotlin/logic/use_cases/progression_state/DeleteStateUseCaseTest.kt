@file:OptIn(ExperimentalUuidApi::class)

package logic.use_cases.progression_state

import helper.progression_state_helper.createDummyState
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import logic.exceptions.ProgressionStateNotFoundException
import logic.repositories.ProgressionStateRepository
import net.thechance.logic.validators.progressionStateValidations.ProgressionStateValidator
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.uuid.ExperimentalUuidApi

class DeleteStateUseCaseTest {

    lateinit var deleteProgressionStateUseCase: DeleteProgressionStateUseCase
    val stateRepository: ProgressionStateRepository = mockk(relaxed = true)
    var progressionStateValidator: ProgressionStateValidator = mockk(relaxed = true)


    @BeforeEach
    fun setUp() {
        deleteProgressionStateUseCase = DeleteProgressionStateUseCase(stateRepository, progressionStateValidator)
    }

    @Test
    fun `should throw exception when validation fails`() {
        runTest {
            // given
            val progressionStateId = createDummyState.dummyState().id
            val validationException = IllegalArgumentException("State cannot be deleted")

            coEvery { progressionStateValidator.validateProgressionStateAlreadyExists(progressionStateId) } throws
                    ProgressionStateNotFoundException()

            // when & then
            assertThrows<ProgressionStateNotFoundException> {
                deleteProgressionStateUseCase.execute(progressionStateId)
            }
        }
    }

    @Test
    fun `should call validetor to check existance when want to delete state `() {
        runTest {
            //given
            val dummyState = createDummyState.dummyState()
            //when
            coEvery { progressionStateValidator.validateProgressionStateAlreadyExists(dummyState.id) } returns true
            //then
            deleteProgressionStateUseCase.execute(dummyState.id)

            coVerify { progressionStateValidator.validateProgressionStateAlreadyExists(dummyState.id) }

        }
    }

    @Test
    fun `should delete progression state when validation succeeds`() {
        runTest {
            // given
            val progressionStateId = createDummyState.dummyState().id

            coEvery { progressionStateValidator.validateProgressionStateAlreadyExists(progressionStateId) } returns true // No exception, validation succeeds

            // when
            deleteProgressionStateUseCase.execute(progressionStateId)

            // then
            coVerify { stateRepository.deleteProgressionState(progressionStateId) }
        }
    }
}

