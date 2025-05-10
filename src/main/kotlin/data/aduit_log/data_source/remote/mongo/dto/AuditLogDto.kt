package net.thechance.data.aduit_log.data_source.remote.mongo.dto

import org.bson.codecs.pojo.annotations.BsonId
import java.util.*

data class AuditLogDto(
    @BsonId val id: String = UUID.randomUUID().toString(),
    val entityType: String,
    val entityId: String,
    val description: String,
    val userName: String,
    val createdAt: String
)