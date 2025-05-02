package logic.use_cases.state

import helper.task_helper.createDummyState
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import logic.repositories.StatesRepository
import net.thechance.logic.entities.State
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetStatesByProjectIdUseCaseTest {

    lateinit var getStatesByProjectId: GetStatesByProjectIdUseCase
    lateinit var statesRepository: StatesRepository

    @BeforeEach
    fun setUp() {
        statesRepository = mockk(relaxed = true)
        getStatesByProjectId = GetStatesByProjectIdUseCase(statesRepository)
    }

    @Test
    fun `should return states for the given project ID`() {
        // Given
        val projectId = "projectId"
        val states = listOf(
            State(id = "1", name = "State 1", projectId = "projectId"),
            State(id = "2", name = "State 2", projectId = "projectId"),
            State(id = "3", name = "State 3", projectId = "project-456")
        )
        every { statesRepository.getStates() } returns Result.success(states)

        // When
        val result = getStatesByProjectId.execute(projectId)

        // Then
        assertEquals(2, result?.size)

    }

}