@file:OptIn(ExperimentalUuidApi::class)

package net.thechance.data.progression_state.data_source.remote.mongo.dto

import org.bson.codecs.pojo.annotations.BsonId
import java.util.*
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

data class ProgressionStateDto(
	@BsonId
	val id: Uuid = Uuid.random(),
	val name: String,
	val projectId: Uuid,
)