package net.thechance.data.user.data_source.local

import logic.entities.User
import data.csv_file_handle.CsvFileHandler
import data.csv_file_handle.CsvFileParser
import data.user.data_source.UsersDataSource
import net.thechance.logic.exceptions.UserAlreadyExistsException
import net.thechance.logic.exceptions.UserNotFoundException

class UsersFileDataSource(
    private val userFileHandler: CsvFileHandler,
    private val csvFileParser: CsvFileParser<User>
) : UsersDataSource {

    override suspend fun createUser(user: User) {
        val users = getAllUsers()
        if (users.any {
                it.name == user.name
            }) {
            throw UserAlreadyExistsException()
        }
        val record = csvFileParser.toCsvRecord(user)
        userFileHandler.appendRecord(record)
    }

    override suspend fun getUserByUsername(userName: String): User {
        val users = getAllUsers()
         return users.find { it.name == userName } ?: throw UserNotFoundException()
    }

     override suspend fun getAllUsers(): List<User> {
        return userFileHandler.readRecords().map { record ->
            csvFileParser.parseRecord(record)
        }
    }
}