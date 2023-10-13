package how.realworld.server.model.impl

import how.realworld.server.model.User
import how.realworld.server.model.Users
import org.springframework.stereotype.Service

@Service
open class UsersImpl: Users {
    override fun getById(id: String): User? {
        TODO("Not yet implemented")
    }

    override fun getByEmail(email: String): User? {
        TODO("Not yet implemented")
    }
}