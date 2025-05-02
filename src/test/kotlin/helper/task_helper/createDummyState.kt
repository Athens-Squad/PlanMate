package helper.task_helper

import net.thechance.logic.entities.State

object createDummyState {
    fun dummyState(
        id: String = "12",
        name: String = "TODO",
        projectId: String = "2"
    ): State {
        return State(
            id = id,
            name = name,
            projectId = projectId
        )
    }
}








