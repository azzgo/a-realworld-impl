package how.realworld.server.controller.dto

data class UserUpdateRequestDto(
        val user: UserUpdateRequestDtoUserField
)

data class UserUpdateRequestDtoUserField(
        val username: String? = null,
        val email: String? = null,
        val password: String? = null,
        val bio: String? = null,
        val image: String? = null
)