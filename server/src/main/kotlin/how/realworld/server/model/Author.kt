package how.realworld.server.model

class Author(
        val userId: UserId,
        val username: String,
        val bio: String? = null,
        val image: String? = null,
        val following: Boolean = false
)
