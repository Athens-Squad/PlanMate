package net.thechance.data.tasks.dto

import logic.entities.ProgressionState
import logic.entities.Project
import logic.entities.Task
import org.bson.codecs.pojo.annotations.BsonId
import java.util.*

data class TaskDto(
    @BsonId
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val description: String,
    val currentProgressionState: ProgressionState,
    val projectId: String
) {
    fun toTask() = Task(
        id = id,
        title = title,
        description = description,
        currentProgressionState = currentProgressionState,
        projectId = projectId
    )

    companion object {
        fun fromTask(task:Task) = TaskDto(
            id = task.id,
            title = task.title,
            description = task.description,
            currentProgressionState = task.currentProgressionState,
            projectId = task.projectId
        )
    }
}
