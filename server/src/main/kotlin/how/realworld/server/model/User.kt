package how.realworld.server.model

typealias UserId = String
class User(var userId: UserId? = null,
           val username: String,
           val password: String,
           val email: String,
           val bio: String? = null,
           val image: String? = null) {

    companion object
}
