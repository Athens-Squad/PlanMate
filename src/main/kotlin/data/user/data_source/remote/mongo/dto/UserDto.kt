@file:OptIn(ExperimentalUuidApi::class)

package net.thechance.data.user.data_source.remote.mongo.dto

import org.bson.codecs.pojo.annotations.BsonId
import java.util.*
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

data class UserDto(
	@BsonId val id: Uuid = Uuid.random(),
	val name: String,
	val password: String,
	val userType: String,
	val adminName: String? = null
)