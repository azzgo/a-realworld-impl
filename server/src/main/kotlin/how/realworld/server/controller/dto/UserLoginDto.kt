package how.realworld.server.controller.dto

data class UserLoginDto(
    val user: UserLoginDto_User
)

data class UserLoginDto_User (
    val email: String,
    val password: String
)