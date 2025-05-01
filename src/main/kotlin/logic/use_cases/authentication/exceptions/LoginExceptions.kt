package net.thechance.logic.use_cases.authentication.exceptions


class InvalidCredentialsException(message: String = "Invalid username or password") : Exception(message)
