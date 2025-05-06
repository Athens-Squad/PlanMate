package logic.use_cases.progression_state

import helper.task_helper.createDummyState
import io.mockk.mockk
import io.mockk.verify
import logic.repositories.ProgressionStateRepository
import logic.use_cases.progression_state.progressionStateValidations.ProgressionStateValidator
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class UpdateStateUseCaseTest{
    lateinit var updateState: UpdateProgressionStateUseCase
    val stateRepository: ProgressionStateRepository = mockk(relaxed = true)
    var progressionStateValidator: ProgressionStateValidator =mockk(relaxed = true)


    @BeforeEach
    fun setUp(){
        updateState = UpdateProgressionStateUseCase(stateRepository , progressionStateValidator)
    }

    @Test
    fun`should update state `(){
        //given
        val dummyState = createDummyState.dummyState()
        //when
        updateState.execute(dummyState , dummyState)

        //then
        verify{stateRepository.updateState(dummyState)

        }
    }
}