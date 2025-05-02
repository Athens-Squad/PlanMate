package net.thechance.logic.use_cases.state.stateValidations

import logic.entities.Task
import net.thechance.logic.entities.State

interface StateValidator {
    fun validateProjectExists(projectId: String)
    fun stateIsExist (state: State)
    fun validateStateFieldsIsNotBlankOrThrow(state : State)


}




