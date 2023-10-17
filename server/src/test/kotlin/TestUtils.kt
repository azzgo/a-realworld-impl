import how.realworld.server.model.User

fun createUser(userId: String? = null, username: String = "jake", email: String = "jake@jake.jake", bio: String? = null, image: String? = null): User {
    return User(userId = userId, username = username, email = email, bio = bio, image = image, password = "password")
}