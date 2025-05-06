package logic.use_cases.progression_state
import helper.task_helper.createDummyState
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import logic.repositories.ProgressionStateRepository
import logic.use_cases.progression_state.progressionStateValidations.ProgressionStateValidator
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CreateStateUseCaseTest{

 lateinit var createProgressionStateUseCase: CreateProgressionStateUseCase
 val stateRepository: ProgressionStateRepository = mockk(relaxed = true)
 var progressionStateValidator: ProgressionStateValidator =mockk(relaxed = true)


 @BeforeEach
 fun setUp(){
  createProgressionStateUseCase = CreateProgressionStateUseCase(stateRepository , progressionStateValidator)
 }

 @Test
 fun`should create state when call create new state `(){
  //given
  val dummyState = createDummyState.dummyState()
  //when
  createProgressionStateUseCase.execute(dummyState)

  //then
  verify{stateRepository.createState(dummyState)}

 }

 @Test
 fun `should create state successfully when project is exist `() {
  //given
  val state = createDummyState.dummyState()
  //when
    progressionStateValidator.validateProjectExists(state.projectId)
  //then
  val result = createProgressionStateUseCase.execute(state)
  assertEquals(Result.success(Unit), result)
 }
@Test
 fun `should create state successfully when state not exist `() {
  //given
  val state = createDummyState.dummyState()
  //when
  progressionStateValidator.progressionStateIsExist(state.id)
  //then
  val result = createProgressionStateUseCase.execute(state)

  assertEquals(Result.success(Unit), result)
 }
 }


