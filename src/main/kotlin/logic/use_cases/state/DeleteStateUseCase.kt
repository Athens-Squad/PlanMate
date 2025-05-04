package logic.use_cases.state


import logic.repositories.StatesRepository
import logic.entities.ProgressionState
import logic.use_cases.state.stateValidations.StateValidator

class DeleteStateUseCase(
    private val stateRepository: StatesRepository,
    private val stateValidator: StateValidator
) {
    fun execute(stateId: String): Result<Unit> {
        return runCatching {
            stateValidator.stateIsExist(stateId)
            stateRepository.deleteState(stateId)
        }
    }

}