package how.realworld.server.dto

data class UserLoginDto(
    val user: UserLoginDtoUser
)

data class UserLoginDtoUser (
    val email: String,
    val password: String
)