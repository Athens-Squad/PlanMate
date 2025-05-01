package net.thechance.data.authentication.repository

import logic.entities.User
import net.thechance.data.csv_file_handle.CsvFileHandler
import net.thechance.data.csv_file_handle.CsvFileParser
import net.thechance.logic.repositories.AuthenticationRepository
import net.thechance.logic.use_cases.authentication.exceptions.UserNotFoundException

class AuthRepositoryImpl(
    authFileHandler: CsvFileHandler,
    private val csvFileParser: CsvFileParser<User>
): AuthenticationRepository {
    private val users = authFileHandler.readRecords().map { record ->
        csvFileParser.parseRecord(record)
    }

    override fun login(username: String, password: String): Result<User> = runCatching {
        users.find { user->
            user.name == username && user.password == password
        } ?: throw UserNotFoundException()
    }
}