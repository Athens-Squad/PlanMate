package logic.use_cases.state

import logic.repositories.AuditRepository
import logic.repositories.ProjectsRepository
import logic.repositories.StatesRepository
import net.thechance.logic.entities.State
import net.thechance.logic.exceptions.TasksException
import net.thechance.logic.use_cases.state.stateValidations.StateValidator
import net.thechance.logic.use_cases.task.taskvalidations.TaskValidator
import net.thechance.logic.use_cases.task.taskvalidations.TaskValidatorImpl

class CreateStateUseCase(
    private val stateRepository: StatesRepository,
   private val stateValidator: StateValidator
) {
    fun execute(state: State): Result<Unit> {
        return runCatching {

            stateValidator.validateProjectExists(state.projectId)
            stateValidator.stateIsExist(state)
            stateValidator.validateStateFieldsIsNotBlankOrThrow(state)
            stateRepository.createState(state)

        }

    }
}