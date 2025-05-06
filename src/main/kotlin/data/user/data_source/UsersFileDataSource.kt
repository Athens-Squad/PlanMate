package data.user.data_source

import logic.entities.User
import data.csv_file_handle.CsvFileHandler
import data.csv_file_handle.CsvFileParser
import logic.use_cases.authentication.exceptions.UserAlreadyExistsException
import logic.use_cases.authentication.exceptions.UserNotFoundException

class UsersFileDataSource(
    private val userFileHandler: CsvFileHandler,
    private val csvFileParser: CsvFileParser<User>
) : UsersDataSource {

    override fun createUser(user: User) {
        val users = getAllUsers()
        if (users.any {
                it.name == user.name
            }) {
            throw UserAlreadyExistsException()
        }
        val record = csvFileParser.toCsvRecord(user)
        userFileHandler.appendRecord(record)
    }

    override fun getUserByUsername(userName: String): User {
        val users = getAllUsers()
         return users.find { it.name == userName } ?: throw UserNotFoundException()
    }

     override fun getAllUsers(): List<User> {
        return userFileHandler.readRecords().map { record ->
            csvFileParser.parseRecord(record)
        }
    }
}