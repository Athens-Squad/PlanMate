package net.thechance.logic.exceptions
open class AdminOrMateRegistrationException(message: String) : Exception(message)

class InvalidUsernameException :
    AdminOrMateRegistrationException("Registration failed: Username is invalid. It must meet the required format.")

class InvalidPasswordException :
    AdminOrMateRegistrationException("Registration failed: Password is invalid. It must meet security requirements.")

class NotAnAdminUserException :
    AdminOrMateRegistrationException("Registration failed: User type is not 'ADMIN'. Only admin users can be registered here.")
class NotAnMateUserException :
    AdminOrMateRegistrationException("Registration failed: User type is not 'Mate'. Only mate users can be registered here.")

class AdminUsernameAlreadyExistsException :
    AdminOrMateRegistrationException("Registration failed: An admin user with the same username already exists.")

class MateUsernameAlreadyExistsException :
    AdminOrMateRegistrationException("Registration failed: A Mate user with the same username already exists.")
