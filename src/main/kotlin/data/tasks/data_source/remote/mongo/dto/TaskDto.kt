package net.thechance.data.tasks.data_source.remote.mongo.dto

import logic.entities.ProgressionState
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
)
