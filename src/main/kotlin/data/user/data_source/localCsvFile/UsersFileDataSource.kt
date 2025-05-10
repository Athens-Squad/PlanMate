package net.thechance.data.user.data_source.localCsvFile

import data.user.data_source.UsersDataSource
import data.user.data_source.remote.mongo.mapper.toUser
import data.user.data_source.remote.mongo.mapper.toUserDto
import data.utils.csv_file_handle.CsvFileHandler
import data.utils.csv_file_handle.CsvFileParser
import logic.entities.User
import logic.exceptions.UserAlreadyExistsException
import logic.exceptions.UserNotFoundException
import net.thechance.data.user.data_source.localCsvFile.dto.UserCsvDto
import net.thechance.data.user.data_source.localCsvFile.mapper.toUser
import net.thechance.data.user.data_source.localCsvFile.mapper.toUserCsvDto
import net.thechance.data.user.data_source.remote.mongo.dto.UserDto

class UsersFileDataSource(
    private val userFileHandler: CsvFileHandler,
    private val csvFileParser: CsvFileParser<UserCsvDto>
) : UsersDataSource {

    override suspend fun createUser(user: User, password: String) {
        val users = getAllUsers()
        if (
            users.any {
                it.name == user.name
            }
        ) {
            throw UserAlreadyExistsException()
        }
        val record = csvFileParser.toCsvRecord(user.toUserCsvDto(password))
        userFileHandler.appendRecord(record)
    }

    override suspend fun getUserByUsername(userName: String): User {
        val users = getAllUsers()
        return users.find { it.name == userName }?.toUser() ?: throw UserNotFoundException()
    }

    override suspend fun getAllUsers(): List<UserDto> {
        return userFileHandler.readRecords().map { record ->
            val userCsvDto = csvFileParser.parseRecord(record)
            userCsvDto.toUser().toUserDto(userCsvDto.password)
        }
    }
}