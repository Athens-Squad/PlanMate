package logic.use_cases.state

import helper.task_helper.createDummyState
import io.mockk.mockk
import io.mockk.verify
import logic.repositories.StatesRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class UpdateStateUseCaseTest{
  lateinit var updateState: UpdateStateUseCase
  val stateRepository: StatesRepository = mockk(relaxed = true)


  @BeforeEach
  fun setUp(){
   updateState = UpdateStateUseCase(stateRepository)
  }

  @Test
  fun`should update state `(){
   //given
   val dummyState = createDummyState.dummyState()
   //when
   updateState.updateState(dummyState.id , dummyState)

   //then
   verify{stateRepository.updateStateInFile(dummyState)

  }
 }
 }