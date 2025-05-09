package net.thechance.data.user.data_source.remote.mongo.dto

import org.bson.codecs.pojo.annotations.BsonId
import java.util.*

data class UserDto(
	@BsonId val id: String = UUID.randomUUID().toString(),
	val name: String,
	val password: String,
	val userType: String,
	val adminName: String? = null
)