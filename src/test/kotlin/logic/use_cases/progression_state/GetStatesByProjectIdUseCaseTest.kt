@file:OptIn(ExperimentalUuidApi::class)

package logic.use_cases.progression_state

import helper.progression_state_helper.createDummyState
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import logic.repositories.ProgressionStateRepository
import net.thechance.logic.use_cases.progression_state.progressionStateValidations.ProgressionStateValidator
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.uuid.ExperimentalUuidApi

class GetStatesByProjectIdUseCaseTest {

    private lateinit var getStatesByProjectId: GetProgressionStatesByProjectIdUseCase
    private lateinit var progressionStateRepository: ProgressionStateRepository
    private lateinit var validator: ProgressionStateValidator

    @BeforeEach
    fun setUp() {
        progressionStateRepository = mockk(relaxed = true)
        validator = mockk()
        getStatesByProjectId = GetProgressionStatesByProjectIdUseCase(validator, progressionStateRepository)
    }

    @Test
    fun `should return states for the given project ID`() {
        runTest {
            // Given
            val state = createDummyState.dummyState()
            val states = listOf(
                state,
                state.copy(name = "done")
            )

            coEvery { validator.validateProjectExists(state.projectId) } returns true
            coEvery { progressionStateRepository.getProgressionStatesByProjectId(state.projectId) } returns states

            // When
            val result = getStatesByProjectId.execute(state.projectId)

            // Then
            assertEquals(2, result.size)
        }
    }

}

