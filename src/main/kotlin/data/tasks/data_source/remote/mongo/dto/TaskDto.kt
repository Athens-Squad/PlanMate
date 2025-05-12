@file:OptIn(ExperimentalUuidApi::class)

package net.thechance.data.tasks.data_source.remote.mongo.dto

import logic.entities.ProgressionState
import org.bson.codecs.pojo.annotations.BsonId
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

data class TaskDto(
    @BsonId
    val id: Uuid = Uuid.random(),
    val name: String,
    val description: String,
    val currentProgressionState: ProgressionState,
    val projectId: Uuid
)
