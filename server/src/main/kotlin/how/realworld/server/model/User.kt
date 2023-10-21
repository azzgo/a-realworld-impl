package how.realworld.server.model

import how.realworld.server.repository.mapper.UserMapper

typealias UserId = String
class User(var userId: UserId? = null,
           val username: String,
           val password: String,
           val email: String,
           val bio: String? = null,
           val image: String? = null) {

    companion object
}

fun User.toMapper(): UserMapper = UserMapper(
   id = userId,
    username = username,
    password = password,
    email = email,
    bio = bio,
    image = image,
)