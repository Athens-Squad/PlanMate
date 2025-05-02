package logic.use_cases.state.stateValidations


import logic.entities.ProgressionState

interface StateValidator {
    fun validateProjectExists(projectId: String)
    fun stateIsExist (state: ProgressionState)
    fun validateStateFieldsIsNotBlankOrThrow(state : ProgressionState)


}




