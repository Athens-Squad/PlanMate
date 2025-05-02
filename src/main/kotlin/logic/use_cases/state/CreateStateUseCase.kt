package logic.use_cases.state

import logic.repositories.StatesRepository
import logic.entities.ProgressionState
import logic.use_cases.state.stateValidations.StateValidator

class CreateStateUseCase(
    private val stateRepository: StatesRepository,
   private val stateValidator: StateValidator
) {
    fun execute(state: ProgressionState): Result<Unit> {
        return runCatching {

            stateValidator.validateProjectExists(state.projectId)
            stateValidator.stateIsExist(state)
            stateValidator.validateStateFieldsIsNotBlankOrThrow(state)
            stateRepository.createState(state)

        }

    }
}