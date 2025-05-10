@file:OptIn(ExperimentalUuidApi::class)

package net.thechance.data.aduit_log.data_source.remote.mongo.dto

import org.bson.codecs.pojo.annotations.BsonId
import java.util.*
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

data class AuditLogDto(
    @BsonId val id: Uuid = Uuid.random(),
    val entityType: String,
    val entityId: Uuid,
    val description: String,
    val userName: String,
    val createdAt: String
)