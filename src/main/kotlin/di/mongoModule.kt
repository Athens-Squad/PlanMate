package net.thechance.di

import com.mongodb.kotlin.client.coroutine.MongoClient
import com.mongodb.kotlin.client.coroutine.MongoCollection
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import data.user.data_source.UsersDataSource
import net.thechance.data.user.data_source.remote.UserDto
import net.thechance.data.user.data_source.remote.UserMongoDataSource
import net.thechance.data.utils.loadEnvironmentVariable
import org.koin.dsl.module

val mongoModule = module {
    single<MongoClient> {
        MongoClient.create(loadEnvironmentVariable("MONGODB_URI"))
    }

    single<MongoDatabase> {
        get<MongoClient>().getDatabase("planmate")
    }

    single<MongoCollection<UserDto>> { get<MongoDatabase>().getCollection<UserDto>("users") }



}