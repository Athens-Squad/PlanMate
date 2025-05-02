package logic.use_cases.state
import helper.task_helper.createDummyState
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import logic.repositories.StatesRepository
import logic.use_cases.state.stateValidations.StateValidator
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CreateStateUseCaseTest{

 lateinit var createStateUseCase: CreateStateUseCase
 val stateRepository: StatesRepository = mockk(relaxed = true)
 var stateValidator: StateValidator =mockk(relaxed = true)


 @BeforeEach
 fun setUp(){
  createStateUseCase = CreateStateUseCase(stateRepository , stateValidator)
 }

 @Test
 fun`should create state when call create new state `(){
  //given
  val dummyState = createDummyState.dummyState()
  //when
  createStateUseCase.execute(dummyState)

  //then
  verify{stateRepository.createState(dummyState)}

 }

 @Test
 fun `should create state successfully when project is exist `() {
  //given
  val state = createDummyState.dummyState()
  //when
    stateValidator.validateProjectExists(state.projectId)
  //then
  val result = createStateUseCase.execute(state)
  assertEquals(Result.success(Unit), result)
 }
@Test
 fun `should create state successfully when state not exist `() {
  //given
  val state = createDummyState.dummyState()
  //when
  stateValidator.stateIsExist(state)
  //then
  val result = createStateUseCase.execute(state)

  assertEquals(Result.success(Unit), result)
 }
 }


