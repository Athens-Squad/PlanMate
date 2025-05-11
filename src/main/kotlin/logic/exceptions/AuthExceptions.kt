package logic.exceptions


open class AuthenticationException(message: String) : Exception(message)


class InvalidCredentialsException() :
    AuthenticationException("Login failed: The provided username or password is incorrect.")

class UserNotFoundException() :
    AuthenticationException("User lookup failed: No user exists with the provided information.")

class UserAlreadyExistsException() :
    AuthenticationException("Registration failed: A user with this username already exists.")