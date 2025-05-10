@file:OptIn(ExperimentalUuidApi::class)

package net.thechance.data.tasks.data_source.remote.mongo.dto

import logic.entities.ProgressionState
import logic.entities.Task
import org.bson.codecs.pojo.annotations.BsonId
import java.util.*
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

data class TaskDto(
    @BsonId
    val id: Uuid = Uuid.random(),
    val title: String,
    val description: String,
    val currentProgressionState: ProgressionState,
    val projectId: Uuid
)
