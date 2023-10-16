package how.realworld.server.controller.dto

data class UserLoginDto(
    val user: UserLoginDtoUserField
)

data class UserLoginDtoUserField (
    val email: String,
    val password: String
)