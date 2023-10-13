package how.realworld.server.dto

data class UserLoginResponseDto(
    val user: UserLoginResponseDtoUser
)

data class UserLoginResponseDtoUser (
    val email: String,
    val token: String,
    val username: String,
    val bio: String?,
    val image: String?
)