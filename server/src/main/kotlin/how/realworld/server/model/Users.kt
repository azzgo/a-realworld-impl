package how.realworld.server.model

interface Users {
   fun getById(id: String): User?
   fun getByEmail(email: String): User?
   fun generateTokenForUser(user: User): String
}