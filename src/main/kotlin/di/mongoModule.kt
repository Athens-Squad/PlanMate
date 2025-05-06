package net.thechance.di

import com.mongodb.kotlin.client.coroutine.MongoClient
import com.mongodb.kotlin.client.coroutine.MongoCollection
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import net.thechance.data.utils.loadEnvironmentVariable
import org.koin.dsl.module

val mongoModule = module {
    single<MongoClient> {
        MongoClient.create(loadEnvironmentVariable("MONGODB_URI"))
    }

    single<MongoDatabase> {
        get<MongoClient>().getDatabase("planmate")
    }

	single<MongoCollection<MongoDatabase>> {
		get<MongoDatabase>().getCollection("progressionStates", MongoDatabase::class.java)
	}
}