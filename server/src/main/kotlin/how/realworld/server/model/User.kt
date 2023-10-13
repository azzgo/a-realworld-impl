package how.realworld.server.model

class User(val username: String, val password: String, val email: String, val bio: String, val image: String) {
    fun generateToken(): String {
        return ""
    }

    fun matched(encodedPassword: String): Boolean {
        return encodedPassword == password;
    }
}
