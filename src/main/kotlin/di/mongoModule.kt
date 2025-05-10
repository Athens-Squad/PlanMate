package net.thechance.di

import com.mongodb.kotlin.client.coroutine.MongoClient
import com.mongodb.kotlin.client.coroutine.MongoCollection
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import net.thechance.data.aduit_log.data_source.remote.mongo.dto.AuditLogDto
import net.thechance.data.progression_state.data_source.remote.mongo.dto.ProgressionStateDto
import data.projects.data_source.remote.mongo.dto.ProjectDto
import net.thechance.data.tasks.data_source.remote.mongo.dto.TaskDto
import net.thechance.data.user.data_source.remote.mongo.dto.UserDto
import net.thechance.data.utils.MongoProvider
import net.thechance.data.utils.loadEnvironmentVariable
import org.koin.core.qualifier.named
import org.koin.dsl.module

val mongoModule = module {

    single<MongoClient> {
        MongoProvider.createClient(loadEnvironmentVariable("MONGODB_URI"))
    }

    single<MongoDatabase> {
        get<MongoClient>().getDatabase("planmate")
    }


    single<MongoCollection<UserDto>>(named("usersCollection")) {
        MongoProvider.getCollection(
            database = get(),
            collectionName = "users",
            documentClass = UserDto::class.java
        )
    }

    single<MongoCollection<ProjectDto>>(named("projectsCollection")) {
        MongoProvider.getCollection(
            database = get(),
            collectionName = "projects",
            documentClass = ProjectDto::class.java
        )
    }

    single<MongoCollection<TaskDto>>(named("tasksCollection")) {
        MongoProvider.getCollection(
            database = get(),
            collectionName = "tasks",
            documentClass = TaskDto::class.java
        )
    }

    single<MongoCollection<ProgressionStateDto>>(named("progressionStatesCollection")) {
        MongoProvider.getCollection(
            database = get(),
            collectionName = "progression_states",
            documentClass = ProgressionStateDto::class.java
        )
    }

    single<MongoCollection<AuditLogDto>>(named("auditLogsCollection")) {
        MongoProvider.getCollection(
            database = get(),
            collectionName = "audit_logs",
            documentClass = AuditLogDto::class.java
        )
    }

}