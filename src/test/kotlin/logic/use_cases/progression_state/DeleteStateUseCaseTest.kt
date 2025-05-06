package logic.use_cases.progression_state

import helper.task_helper.createDummyState
import io.mockk.mockk
import io.mockk.verify
import logic.repositories.ProgressionStateRepository
import logic.use_cases.progression_state.progressionStateValidations.ProgressionStateValidator
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class DeleteStateUseCaseTest {

 lateinit var deleteProgressionStateUseCase: DeleteProgressionStateUseCase
 val stateRepository: ProgressionStateRepository = mockk(relaxed = true)
 var progressionStateValidator: ProgressionStateValidator =mockk(relaxed = true)


 @BeforeEach
 fun setUp() {
  deleteProgressionStateUseCase = DeleteProgressionStateUseCase(stateRepository , progressionStateValidator)
 }

 @Test
 fun `should call delete State From File function when delete state `() {
  //given
  val dummyState = createDummyState.dummyState()
  //when
  deleteProgressionStateUseCase.execute(dummyState.id)

  //then
  verify { stateRepository.deleteState(dummyState.id) }

 }
 @Test
 fun `should call validetor to check existance when want to delete state `() {
  //given
  val dummyState = createDummyState.dummyState()
  //when
  deleteProgressionStateUseCase.execute(dummyState.id)

  //then
  verify { progressionStateValidator.progressionStateIsExist(dummyState.id) }
 }
}

