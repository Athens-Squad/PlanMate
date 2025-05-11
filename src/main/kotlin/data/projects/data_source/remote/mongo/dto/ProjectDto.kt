@file:OptIn(ExperimentalUuidApi::class)

package data.projects.data_source.remote.mongo.dto

import org.bson.codecs.pojo.annotations.BsonId
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

data class ProjectDto(
    @BsonId
    val id: Uuid = Uuid.random(),
    val name: String,
    val description: String,
    val createdByUserName: String
)
