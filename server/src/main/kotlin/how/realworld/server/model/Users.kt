package how.realworld.server.model

interface Users {
    fun getById(id: UserId): User?
    fun getByEmail(email: String): User?
    fun generateTokenForUser(user: User): String
    fun create(email: String, username: String, rawPassword: String): User
    fun update(userId: String,
       username: String? = null,
       email: String? = null,
       password: String? = null,
       bio: String? = null,
       image: String? = null
    ): User
    fun checkUserExist(email: String, username: String): UserExist
}

data class UserExist(
        val email: Boolean,
        val username: Boolean
)