package net.thechance.data.projects.dto

import org.bson.codecs.pojo.annotations.BsonId
import java.util.*

data class ProjectDto(
    @BsonId
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val description: String,
    val createdBy: String
)
