package net.thechance.di

import com.mongodb.kotlin.client.coroutine.MongoClient
import com.mongodb.kotlin.client.coroutine.MongoCollection
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import net.thechance.data.aduit_log.dto.AuditLogDto
import net.thechance.data.progression_state.dto.ProgressionStateDto
import net.thechance.data.projects.dto.ProjectDto
import net.thechance.data.user.data_source.remote.UserDto
import net.thechance.data.utils.loadEnvironmentVariable
import org.koin.dsl.module

val mongoModule = module {
    single<MongoClient> {
        MongoClient.create(loadEnvironmentVariable("MONGODB_URI"))
    }

    single<MongoDatabase> {
        get<MongoClient>().getDatabase("planmate")
    }

    single<MongoCollection<ProjectDto>> { get<MongoDatabase>().getCollection<ProjectDto>("projects") }
    single<MongoCollection<ProgressionStateDto>> {
        get<MongoDatabase>().getCollection("progression_states", ProgressionStateDto::class.java)
    }
    single<MongoCollection<AuditLogDto>> {
        get<MongoDatabase>().getCollection("audit_log", AuditLogDto::class.java)
    }
    single<MongoCollection<UserDto>> { get<MongoDatabase>().getCollection<UserDto>("users") }



}