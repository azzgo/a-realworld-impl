package how.realworld.server.controller.dto

data class UserRegisterDto(
    val user: UserRegisterDtoUserField
)

data class UserRegisterDtoUserField (
    val username: String,
    val password: String,
    val email: String
)
