package logic.use_cases.state

import helper.task_helper.createDummyState
import io.mockk.mockk
import io.mockk.verify
import logic.repositories.StatesRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CreateStateUseCaseTest{

  lateinit var createStateUseCase: CreateStateUseCase
   val stateRepository: StatesRepository = mockk(relaxed = true)


  @BeforeEach
  fun setUp(){
   createStateUseCase = CreateStateUseCase(stateRepository)
  }

    @Test
    fun`should create state when call create new state `(){
        //given
        val dummyState = createDummyState.dummyState()
        //when
        createStateUseCase.createNewState(dummyState)

        //then
        verify{stateRepository.createStateInFile(dummyState)}

    }

 }