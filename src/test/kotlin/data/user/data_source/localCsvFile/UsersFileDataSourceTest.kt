@file:OptIn(ExperimentalUuidApi::class)

package data.user.data_source.localCsvFile


import data.user.data_source.remote.mongo.mapper.toUserDto
import data.utils.csv_file_handle.CsvFileHandler
import data.utils.csv_file_handle.CsvFileParser
import helper.authentication_helper.FakeUser
import io.mockk.*
import kotlinx.coroutines.test.runTest
import logic.exceptions.UserAlreadyExistsException
import logic.exceptions.UserNotFoundException
import net.thechance.data.user.data_source.localCsvFile.UsersFileDataSource
import net.thechance.data.user.data_source.localCsvFile.dto.UserCsvDto
import net.thechance.data.user.data_source.localCsvFile.mapper.toUser
import net.thechance.data.user.data_source.localCsvFile.mapper.toUserCsvDto
import kotlin.test.*
import kotlin.uuid.ExperimentalUuidApi


class UsersFileDataSourceTest {

    private val fileHandler = mockk<CsvFileHandler>()
    private val parser = mockk<CsvFileParser<UserCsvDto>>()
    private lateinit var dataSource: UsersFileDataSource
    private val user = FakeUser.createUser
    private val password = "password"
    private val dto = user.toUserCsvDto(password)
    private val csvRecord = "${user.id},${user.name},$password,${user.type}"

    @BeforeTest
    fun setup() {
        dataSource = UsersFileDataSource(fileHandler, parser)
    }

    @Test
    fun `create user success and append record`() = runTest {
        coEvery { fileHandler.readRecords() } returns emptyList()
        every { parser.toCsvRecord(user.toUserCsvDto(password)) } returns csvRecord
        coEvery { fileHandler.appendRecord(csvRecord) } just Runs

        dataSource.createUser(user, password)

        coVerify { fileHandler.appendRecord(csvRecord) }
    }

    @Test
    fun `should throw UserAlreadyExistsException when user already exists`() = runTest {
        coEvery { fileHandler.readRecords() } returns listOf(csvRecord)
        every { parser.parseRecord(csvRecord) } returns dto

        val exception = assertFailsWith<UserAlreadyExistsException> {
            dataSource.createUser(user, password)
        }

        assertTrue(exception.message == "User already exists")
    }

    @Test
    fun `should return user when exists`() = runTest {
        coEvery { fileHandler.readRecords() } returns listOf(csvRecord)
        every { parser.parseRecord(csvRecord) } returns dto

        val result = dataSource.getUserByUsername(user.name)

        assertEquals(user, result)
    }

    @Test
    fun `should throw UserNotFoundException when not found`() = runTest {
        coEvery { fileHandler.readRecords() } returns listOf(csvRecord)
        every { parser.parseRecord(csvRecord) } returns dto

        val exception = assertFailsWith<UserNotFoundException> {
            dataSource.getUserByUsername("Ahmed")
        }

        assertEquals(exception.message, "User not found")
    }


}