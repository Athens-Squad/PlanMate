package net.thechance.data.utils

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.kotlin.client.coroutine.MongoClient
import com.mongodb.kotlin.client.coroutine.MongoCollection
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import org.bson.codecs.configuration.CodecRegistries.fromProviders
import org.bson.codecs.configuration.CodecRegistries.fromRegistries

object MongoProvider {
    fun createClient(connectionString: String): MongoClient {
        return MongoClient.create(
            MongoClientSettings.builder()
                .applyConnectionString(ConnectionString(connectionString))
                .codecRegistry(
                    fromRegistries(
                        fromProviders(UuidCodecProvider()),
                        MongoClientSettings.getDefaultCodecRegistry()
                    )
                )
                .build()
        )
    }

    fun <T : Any> getCollection(
        database: MongoDatabase,
        collectionName: String,
        documentClass: Class<T>
    ): MongoCollection<T> {
        return database.getCollection(collectionName, documentClass)
    }
}