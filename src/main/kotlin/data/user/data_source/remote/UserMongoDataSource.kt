package net.thechance.data.user.data_source.remote

import com.mongodb.client.model.Filters
import com.mongodb.kotlin.client.coroutine.MongoCollection
import data.user.data_source.UsersDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.withContext
import logic.entities.User
import kotlinx.coroutines.flow.firstOrNull
import logic.use_cases.authentication.exceptions.UserAlreadyExistsException
import logic.use_cases.authentication.exceptions.UserNotFoundException


class UserMongoDataSource(private val userCollection:MongoCollection<UserDto>):UsersDataSource {


    override suspend fun createUser(user: User) {

        val existing = userCollection
            .find(Filters.eq("name", user.name))
            .firstOrNull()

        if (existing != null) throw UserAlreadyExistsException()

        userCollection.insertOne(UserDto.fromUser(user))



    }

    override suspend fun getUserByUsername(userName: String): User {
        val dto = userCollection
            .find(Filters.eq("name", userName))
            .firstOrNull()
            ?: throw UserNotFoundException()

        return dto.toUser()
    }

    override suspend fun getAllUsers(): List<User> {
        return userCollection
            .find()
            .toList()
            .map { it.toUser() }
    }
}