package data.user.repository

import data.user.data_source.UsersDataSource
import logic.entities.User
import logic.repositories.UserRepository

class UserRepositoryImpl(
    private val usersDataSource: UsersDataSource
) : UserRepository, UsersDataSource by usersDataSource