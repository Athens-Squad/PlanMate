package logic.use_cases.state

import helper.task_helper.createDummyState
import io.mockk.mockk
import io.mockk.verify
import logic.repositories.StatesRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetStateByIdUseCaseTest {

    lateinit var getStateByIdUseCase: GetStateByIdUseCase
    val stateRepository: StatesRepository = mockk(relaxed = true)


    @BeforeEach
    fun setUp() {
        getStateByIdUseCase = GetStateByIdUseCase(stateRepository)
    }

    @Test
    fun `should call get state function when want to optain state `() {
        //given
        val dummyState = createDummyState.dummyState()
        //when
        getStateByIdUseCase.getStateById(dummyState.id)

        //then
        verify(exactly = 1) { stateRepository.getStatesFromFile() }
    }
}
