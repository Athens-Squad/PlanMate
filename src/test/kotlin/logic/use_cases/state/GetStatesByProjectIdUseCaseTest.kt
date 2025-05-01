package logic.use_cases.state

import helper.task_helper.createDummyState
import io.mockk.mockk
import io.mockk.verify
import logic.repositories.StatesRepository
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
    fun `should call `() {
        //given
        val dummyState = createDummyState.dummyState()
        //when
        getStatesByProjectId.getStateByProjectId(dummyState.projectId)
        //then
        verify(exactly = 1) { statesRepository.getStatesFromFile() }

    }
}