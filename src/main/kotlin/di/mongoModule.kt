package net.thechance.di

import com.mongodb.kotlin.client.coroutine.MongoClient
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import net.thechance.data.aduit_log.data_source.remote.mongo.dto.AuditLogDto
import net.thechance.data.progression_state.data_source.remote.mongo.dto.ProgressionStateDto
import data.projects.data_source.remote.mongo.dto.ProjectDto
import net.thechance.data.tasks.data_source.remote.mongo.dto.TaskDto
import net.thechance.data.user.data_source.remote.mongo.dto.UserDto
import net.thechance.data.utils.loadEnvironmentVariable
import org.koin.core.qualifier.named
import org.koin.dsl.module

val mongoModule = module {
    single<MongoClient> {
        MongoClient.create(loadEnvironmentVariable("MONGODB_URI"))
    }

    single<MongoDatabase> {
        get<MongoClient>().getDatabase("planmate")
    }

    single(named("projectsCollection")) { get<MongoDatabase>().getCollection<ProjectDto>("projects") }

    single(named("progressionStatesCollection")) {
        get<MongoDatabase>().getCollection("progression_states", ProgressionStateDto::class.java)
    }

    single(named("tasksCollection")) { get<MongoDatabase>().getCollection<TaskDto>("tasks") }

    single(named("auditLogsCollection")) {
        get<MongoDatabase>().getCollection("audit_log", AuditLogDto::class.java)
    }
    single(named("usersCollection")) { get<MongoDatabase>().getCollection<UserDto>("users", UserDto::class.java) }



}