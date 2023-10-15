package how.realworld.server.controller.dto

import how.realworld.server.model.User

data class UserAuthenticationResponseDto(
        val user: UserAuthenticationResponseDtoUserField
)

data class UserAuthenticationResponseDtoUserField(
        val email: String,
        val token: String,
        val username: String,
        val bio: String?,
        val image: String?
) {
    companion object
}

fun UserAuthenticationResponseDtoUserField.Companion.fromUser(user: User, token: String) = UserAuthenticationResponseDtoUserField(
        email = user.email,
        username = user.username,
        token = token,
        bio = user.bio,
        image = user.image
)