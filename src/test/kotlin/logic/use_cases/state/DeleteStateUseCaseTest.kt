package logic.use_cases.state

import helper.task_helper.createDummyState
import io.mockk.mockk
import io.mockk.verify
import logic.repositories.StatesRepository
import net.thechance.logic.use_cases.state.stateValidations.StateValidator
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class DeleteStateUseCaseTest {

 lateinit var deleteStateUseCase: DeleteStateUseCase
 val stateRepository: StatesRepository = mockk(relaxed = true)
 var stateValidator: StateValidator =mockk(relaxed = true)


 @BeforeEach
 fun setUp() {
  deleteStateUseCase = DeleteStateUseCase(stateRepository , stateValidator)
 }

 @Test
 fun `should call delete State From File function when delete state `() {
  //given
  val dummyState = createDummyState.dummyState()
  //when
  deleteStateUseCase.execute(dummyState)

  //then
  verify { stateRepository.deleteState(dummyState.id) }

 }
 @Test
 fun `should call validetor to check existance when want to delete state `() {
  //given
  val dummyState = createDummyState.dummyState()
  //when
  deleteStateUseCase.execute(dummyState)

  //then
  verify { stateValidator.stateIsExist(dummyState) }
 }
}

