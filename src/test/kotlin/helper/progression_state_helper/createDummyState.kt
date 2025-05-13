@file:OptIn(ExperimentalUuidApi::class)

package helper.progression_state_helper

import logic.entities.ProgressionState
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

object createDummyState {
    fun dummyState(
        id: Uuid = Uuid.random(),
        name: String = "TODO",
        projectId: Uuid = Uuid.random()
    ): ProgressionState {
        return ProgressionState(
            id = id,
            name = name,
            projectId = projectId
        )
    }
}