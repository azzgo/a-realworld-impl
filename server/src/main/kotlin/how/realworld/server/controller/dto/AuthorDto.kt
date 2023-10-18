package how.realworld.server.controller.dto

data class AuthorDto(
        val username: String,
        val bio: String?,
        val image: String?,
        val following: Boolean
) {
    companion object
}