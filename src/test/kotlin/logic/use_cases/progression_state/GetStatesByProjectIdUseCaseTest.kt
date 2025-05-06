package logic.use_cases.progression_state

import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import logic.entities.ProgressionState
import logic.repositories.ProgressionStateRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetStatesByProjectIdUseCaseTest {

    lateinit var getStatesByProjectId: GetProgressionStatesByProjectIdUseCase
    lateinit var progressionStateRepository: ProgressionStateRepository

    @BeforeEach
    fun setUp() {
        progressionStateRepository = mockk(relaxed = true)
        getStatesByProjectId = GetProgressionStatesByProjectIdUseCase(progressionStateRepository)
    }

    @Test
    fun `should return states for the given project ID`() {
        // Given
        val projectId = "projectId"
        val states = listOf(
            ProgressionState(id = "1", name = "State 1", projectId = "projectId"),
            ProgressionState(id = "2", name = "State 2", projectId = "projectId"),
            ProgressionState(id = "3", name = "State 3", projectId = "project-456")
        )
        every { progressionStateRepository.getStates() } returns Result.success(states)

        // When
        val result = getStatesByProjectId.execute(projectId).getOrThrow()

        // Then
        assertEquals(2, result.size)

    }

}