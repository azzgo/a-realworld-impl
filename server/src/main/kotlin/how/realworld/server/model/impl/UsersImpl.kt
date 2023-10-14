package how.realworld.server.model.impl

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import how.realworld.server.model.User
import how.realworld.server.model.Users
import how.realworld.server.repository.UserRepository
import how.realworld.server.repository.mapper.UserMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
open class UsersImpl(private val userRepository: UserRepository, @Value("\${auth.jwt.secret") private val secret: String)
    : Users {

    override fun getById(id: String): User? {
        TODO("Not yet implemented")
    }

    override fun getByEmail(email: String): User? {
        val userMapper = userRepository.findByEmail(email) ?: return null
        return User.from(userMapper)
    }

    override fun generateTokenForUser(user: User): String {
        if (user.userId == null) {
            throw IllegalArgumentException("cannot generate token for non exists user ${user.username}")
        }
        return JWT.create()
                .withClaim("user_id", user.userId)
                .withClaim("username", user.username)
                .withClaim("email", user.username)
                .sign(Algorithm.HMAC256(secret))
    }
}

private fun User.Companion.from(userMapper: UserMapper): User {
    return User(
            userId = userMapper.id,
            email = userMapper.email,
            username = userMapper.username,
            password = userMapper.password,
            bio = userMapper.bio,
            image = userMapper.image
    )
}
