package net.thechance.data.progression_state.data_source.remote.mongo.dto

import org.bson.codecs.pojo.annotations.BsonId
import java.util.*

data class ProgressionStateDto(
	@BsonId
	val id: String = UUID.randomUUID().toString(),
	val name: String,
	val projectId: String,
)