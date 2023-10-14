package how.realworld.server.model


class User(var userId: String? = null,
           val username: String,
           val password: String,
           val email: String,
           val bio: String? = null,
           val image: String? = null) {

    companion object
}
