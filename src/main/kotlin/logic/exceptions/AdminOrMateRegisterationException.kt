package net.thechance.logic.exceptions

open class UserRegistrationException(message: String) : Exception(message)

class InvalidUsernameException :
    UserRegistrationException("Registration failed: Username is invalid. It must meet the required format.")

class InvalidPasswordException :
    UserRegistrationException("Registration failed: Password is invalid. It must meet security requirements.")

class NotAnAdminUserException :
    UserRegistrationException("Registration failed: User type is not 'ADMIN'. Only admin users can be registered here.")
class NotAnMateUserException :
    UserRegistrationException("Registration failed: User type is not 'Mate'. Only mate users can be registered here.")

class AdminUsernameAlreadyExistsException :
    UserRegistrationException("Registration failed: An admin user with the same username already exists.")

class MateUsernameAlreadyExistsException :
    UserRegistrationException("Registration failed: A Mate user with the same username already exists.")
