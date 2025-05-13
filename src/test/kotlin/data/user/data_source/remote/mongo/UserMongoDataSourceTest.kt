@file:OptIn(ExperimentalUuidApi::class)

package data.user.data_source.remote.mongo

import com.google.common.truth.Truth.assertThat
import com.mongodb.client.model.Filters
import com.mongodb.kotlin.client.coroutine.FindFlow
import com.mongodb.kotlin.client.coroutine.MongoCollection
import data.user.data_source.remote.mongo.mapper.toUserDto
import helper.authentication_helper.FakeUser
import io.mockk.*
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import logic.entities.User
import logic.exceptions.UserAlreadyExistsException
import logic.exceptions.UserNotFoundException
import net.thechance.data.user.data_source.remote.mongo.UserMongoDataSource
import net.thechance.data.user.data_source.remote.mongo.dto.UserDto
import org.bson.conversions.Bson
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.test.BeforeTest
import kotlin.test.assertFailsWith
import kotlin.uuid.ExperimentalUuidApi

class UserMongoDataSourceTest {

    private lateinit var userCollection: MongoCollection<UserDto>
    private lateinit var dataSource: UserMongoDataSource
    private lateinit var user: User
    private lateinit var password: String
    private lateinit var dto: UserDto

    @BeforeTest
    fun setup() {
        userCollection = mockk<MongoCollection<UserDto>>()
        dataSource = UserMongoDataSource(userCollection)
        user = FakeUser.createUser
        password = "password"
        dto = user.toUserDto(password)
    }

    @Test
    fun `should throw UserAlreadyExistsException when creating existing user`() = runTest {
        // Given
        val findFlowMock = mockk<FindFlow<UserDto>>(relaxed = true)

        coEvery { findFlowMock.collect(any()) } coAnswers {
            val collector = arg<FlowCollector<UserDto>>(0)
            collector.emit(dto)
        }

        coEvery { findFlowMock.firstOrNull() } returns dto
        every { userCollection.find(any<Bson>()) } returns findFlowMock

        // When / Then
        assertFailsWith<UserAlreadyExistsException> {
            dataSource.createUser(user, password)
        }
    }

    @Test
    fun `should get user by username when exists`() = runTest {
        // Given
        val findFlowMock = mockk<FindFlow<UserDto>>()

        coEvery { findFlowMock.collect(any()) } coAnswers {
            val collector = arg<FlowCollector<UserDto>>(0)
            collector.emit(dto)
        }

        coEvery { findFlowMock.firstOrNull() } returns dto

        // Mock the collection query to return the mocked FindFlow
        every { userCollection.find(any<Bson>()) } returns findFlowMock

        // When
        val result = dataSource.getUserByUsername(user.name)

        // Then
        assertEquals(user.name, result.name)
    }

    @Test
    fun `should throw UserNotFoundException when getting non-existent user`() = runTest {
        // Given
        val findFlowMock = mockk<FindFlow<UserDto>>(relaxed = true)
        coEvery { findFlowMock.firstOrNull() } returns null
        every { userCollection.find(any<Bson>()) } returns findFlowMock

        // When / Then
        assertFailsWith<UserNotFoundException> {
            dataSource.getUserByUsername("nonexistent")
        }
    }

    @Test
    fun `should get all users`() = runTest {
        // Given
        val userList = listOf(dto, dto.copy(name = "Ahmed"))
        val findFlowMock = mockk<FindFlow<UserDto>>() {
            coEvery { toList() } returns userList
        }

        coEvery { findFlowMock.toList() } returns userList
        coEvery { userCollection.find().collect(any()) } coAnswers {
            val collector = arg<FlowCollector<UserDto>>(0)
            collector.emit(dto)
            collector.emit(dto.copy(name = "Ahmed"))
        }

        // When
        val result = dataSource.getAllUsers()

        // Then
        assertEquals(2, result.size)
        assertEquals(user.name, result[0].name)
        assertEquals(userList[1].name, result[1].name)
    }

    @Test
    fun `should return empty list when no users exist`() = runTest {
        // Given
        val findFlowMock = mockk<FindFlow<UserDto>>(relaxed = true)
        coEvery { findFlowMock.toList() } returns emptyList()
        every { userCollection.find() } returns findFlowMock

        // When
        val result = dataSource.getAllUsers()

        // Then
        assertThat(result).isEmpty()
    }

}

