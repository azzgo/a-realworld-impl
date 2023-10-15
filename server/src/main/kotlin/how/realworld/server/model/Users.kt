package how.realworld.server.model

interface Users {
    fun getById(id: String): User?
    fun getByEmail(email: String): User?
    fun generateTokenForUser(user: User): String
    fun createUser(email: String, username: String, rawPassword: String): User
    fun checkUserExist(email: String, username: String): UserExist
}

data class UserExist(
    val email: Boolean,
    val username: Boolean
)