package logic.use_cases.state.stateValidations


import logic.entities.ProgressionState

interface StateValidator {
    fun validateProjectExists(projectId: String)
    fun stateIsExist (stateId: String)
    fun validateStateFieldsIsNotBlankOrThrow(state : ProgressionState)


}




