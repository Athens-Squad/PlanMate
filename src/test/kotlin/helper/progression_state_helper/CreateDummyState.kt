package helper.progression_state_helper

import logic.entities.ProgressionState

object createDummyState {
    fun dummyState(
        id: String = "12",
        name: String = "TODO",
        projectId: String = "2"
    ): ProgressionState {
        return ProgressionState(
            id = id,
            name = name,
            projectId = projectId
        )
    }
}








