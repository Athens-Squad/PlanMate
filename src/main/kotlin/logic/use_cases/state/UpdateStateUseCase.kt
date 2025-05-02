package logic.use_cases.state

import logic.repositories.AuditRepository
import logic.repositories.StatesRepository
import net.thechance.logic.entities.State
import net.thechance.logic.use_cases.state.stateValidations.StateValidator


class UpdateStateUseCase(
    private val stateRepository: StatesRepository,
    private val stateValidator: StateValidator
) {
    fun execute(state: State, updatedState: State) {

        stateValidator.stateIsExist(state)
        stateValidator.validateProjectExists(state.id)
        stateRepository.updateState(updatedState)

    }

}
