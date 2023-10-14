package how.realworld.server.controller.dto

import how.realworld.server.model.User

data class UserLoginResponseDto(
        val user: UserLoginResponseDto_User
)

data class UserLoginResponseDto_User(
        val email: String,
        val token: String,
        val username: String,
        val bio: String?,
        val image: String?
) {
    companion object
}

fun UserLoginResponseDto_User.Companion.fromUser(user: User, token: String) = UserLoginResponseDto_User(
        email = user.email,
        username = user.username,
        token = token,
        bio = user.bio,
        image = user.image
)