package logic.use_cases.state

import logic.entities.ProgressionState
import logic.repositories.StatesRepository
import logic.use_cases.state.stateValidations.StateValidator


class UpdateStateUseCase(
    private val stateRepository: StatesRepository,
    private val stateValidator: StateValidator
) {
    fun execute(state: ProgressionState, updatedState: ProgressionState): Result<Unit> {
        return runCatching {
            stateValidator.stateIsExist(state.id)
            stateValidator.validateProjectExists(state.id)
            stateRepository.updateState(updatedState)
        }

    }

}
