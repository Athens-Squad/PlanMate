package net.thechance.di

import com.mongodb.kotlin.client.coroutine.MongoClient
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import org.koin.dsl.module

val mongoModule = module {
    single<MongoClient> {
        MongoClient.create("mongodb+srv://athens:<athensplanmate>@planmatecluster.caw6br8.mongodb.net/?retryWrites=true&w=majority&appName=PlanMateCluster")
    }

    single<MongoDatabase> {
        get<MongoClient>().getDatabase("planmate")
    }

}