package net.thechance.data.user.data_source.localCsvFile

import data.user.data_source.UsersDataSource
import data.utils.csv_file_handle.CsvFileHandler
import data.utils.csv_file_handle.CsvFileParser
import logic.entities.User
import logic.use_cases.authentication.exceptions.UserAlreadyExistsException
import logic.use_cases.authentication.exceptions.UserNotFoundException
import net.thechance.data.user.dto.UserCsvDto
import net.thechance.data.user.mappers.toUser
import net.thechance.data.user.mappers.toUserCsvDto

class UsersFileDataSource(
	private val userFileHandler: CsvFileHandler,
	private val csvFileParser: CsvFileParser<UserCsvDto>
) : UsersDataSource {

    override suspend fun createUser(user: User) {
        val users = getAllUsers()
        if (users.any {
                it.name == user.name
            }) {
            throw UserAlreadyExistsException()
        }
        val record = csvFileParser.toCsvRecord(user.toUserCsvDto())
        userFileHandler.appendRecord(record)
    }

    override suspend fun getUserByUsername(userName: String): User {
        val users = getAllUsers()
         return users.find { it.name == userName } ?: throw UserNotFoundException()
    }

     override suspend fun getAllUsers(): List<User> {
        return userFileHandler.readRecords().map { record ->
            csvFileParser.parseRecord(record)
	            .toUser()
        }
    }
}