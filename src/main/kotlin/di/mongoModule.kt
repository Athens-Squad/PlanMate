package net.thechance.di

import com.mongodb.kotlin.client.coroutine.MongoClient
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import net.thechance.data.aduit_log.data_source.remote.mongo.dto.AuditLogDto
import net.thechance.data.progression_state.data_source.remote.mongo.dto.ProgressionStateDto
import data.projects.data_source.remote.mongo.dto.ProjectDto
import net.thechance.data.tasks.data_source.remote.mongo.dto.TaskDto
import net.thechance.data.user.data_source.remote.mongo.dto.UserDto
import net.thechance.data.utils.loadEnvironmentVariable
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

val mongoModule = module {

    fun <T : Any> Module.mongoCollection(
        collectionName: String,
        bindingName: String,
        documentClass: Class<T>
    ) {
        single(named(bindingName)) {
            get<MongoDatabase>().getCollection(collectionName, documentClass)
        }
    }

    single<MongoClient> {
        MongoClient.create(loadEnvironmentVariable("MONGODB_URI"))
    }

    single<MongoDatabase> {
        get<MongoClient>().getDatabase("planmate")
    }

    mongoCollection(
        collectionName = "projects",
        bindingName = "projectsCollection",
        documentClass = ProjectDto::class.java
    )
    mongoCollection(
        collectionName = "progression_states",
        bindingName = "progressionStatesCollection",
        documentClass = ProgressionStateDto::class.java
    )
    mongoCollection(
        collectionName = "tasks",
        bindingName = "tasksCollection",
        documentClass = TaskDto::class.java
    )
    mongoCollection(
        collectionName = "audit_log",
        bindingName = "auditLogsCollection",
        documentClass = AuditLogDto::class.java
    )
    mongoCollection(
        collectionName = "users",
        bindingName = "usersCollection",
        documentClass = UserDto::class.java
    )

}