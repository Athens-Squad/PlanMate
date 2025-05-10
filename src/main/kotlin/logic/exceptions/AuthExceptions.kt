package net.thechance.logic.exceptions

open class AuthenticationException(message: String): Exception(message)



class InvalidCredentialsException :
    AuthenticationException("Authentication failed: Invalid username or password.")

class UserNotFoundException :
    AuthenticationException("Authentication failed: The specified user does not exist.")

class UserAlreadyExistsException :
    AuthenticationException("Registration failed: A user with the same credentials already exists.")
