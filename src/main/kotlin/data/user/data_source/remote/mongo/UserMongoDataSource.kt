package net.thechance.data.user.data_source.remote.mongo

import com.mongodb.client.model.Filters
import com.mongodb.kotlin.client.coroutine.MongoCollection
import data.user.data_source.UsersDataSource
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList
import logic.entities.User
import logic.exceptions.UserAlreadyExistsException
import logic.exceptions.UserNotFoundException
import net.thechance.data.user.data_source.remote.mongo.dto.UserDto
import data.user.data_source.remote.mongo.mapper.toUser
import data.user.data_source.remote.mongo.mapper.toUserDto

class UserMongoDataSource(
    private val userCollection: MongoCollection<UserDto>
): UsersDataSource {

    override suspend fun createUser(user: User, password: String) {

        val existing = userCollection
            .find(Filters.eq("name", user.name))
            .firstOrNull()

        if (existing != null) throw UserAlreadyExistsException()

        userCollection.insertOne(user.toUserDto(password))



    }

    override suspend fun getUserByUsername(userName: String): User {
        val dto = userCollection
            .find(Filters.eq("name", userName))
            .firstOrNull()
            ?: throw UserNotFoundException()

        return dto.toUser()
    }

    override suspend fun getAllUsers(): List<UserDto> {
        return userCollection
            .find()
            .toList()
    }
}