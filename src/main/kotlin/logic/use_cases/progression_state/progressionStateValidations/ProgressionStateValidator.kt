package logic.use_cases.progression_state.progressionStateValidations


interface ProgressionStateValidator {
    suspend fun validateProjectExists(projectId: String)
    suspend fun progressionStateIsExist (progressionStateId: String)
    suspend fun validateProgressionStateFieldsIsNotBlankOrThrow(progressionStateId : String)
}




