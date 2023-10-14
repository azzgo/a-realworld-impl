package how.realworld.server.model

class User(var userId: String? = null, val username: String, val password: String, val email: String, val bio: String, val image: String) {

    fun matched(encodedPassword: String): Boolean {
        return encodedPassword == password
    }

    companion object
}
