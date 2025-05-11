package logic.exceptions

open class AuthenticationException(message: String = "Authentication failed") : Exception(message)

class InvalidCredentialsException:
	AuthenticationException( "Invalid username or password")

class UserNotFoundException:
	AuthenticationException( "User not found")

class UserAlreadyExistsException:
	AuthenticationException( "User already exists")
