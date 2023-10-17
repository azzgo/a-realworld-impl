package how.realworld.server.controller.dto

import how.realworld.server.model.User

data class UserAuthenticationWithTokenResponseDto(
        val user: UserAuthenticationResponseDtoUserField
)

data class UserAuthenticationResponseDto(
        val user: UserAuthenticationResponse
)

data class UserAuthenticationResponseDtoUserField(
        override val email: String,
        val token: String,
        override val username: String,
        override val bio: String?,
        override val image: String?
) : UserAuthenticationResponse(email, username, bio, image) {
    companion object
}

open class UserAuthenticationResponse(
        open val email: String,
        open val username: String,
        open val bio: String?,
        open val image: String?
) {
    companion object
}

fun UserAuthenticationResponse.Companion.fromUser(user: User) = UserAuthenticationResponse(
        email = user.email,
        username = user.username,
        bio = user.bio,
        image = user.image
)

fun UserAuthenticationResponseDtoUserField.Companion.fromUser(user: User, token: String) = UserAuthenticationResponseDtoUserField(
        email = user.email,
        username = user.username,
        token = token,
        bio = user.bio,
        image = user.image
)