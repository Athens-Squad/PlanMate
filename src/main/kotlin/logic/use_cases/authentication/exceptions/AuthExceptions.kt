package logic.use_cases.authentication.exceptions


class InvalidCredentialsException(message: String = "Invalid username or password") : Exception(message)
class UserNotFoundException(message: String = "User not found") : Exception(message)
class UserAlreadyExistsException(message: String = "User already exists") : Exception(message)
