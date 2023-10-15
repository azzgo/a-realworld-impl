package how.realworld.server.model.impl

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import how.realworld.server.model.User
import how.realworld.server.model.UserExist
import how.realworld.server.model.Users
import how.realworld.server.repository.UserRepository
import how.realworld.server.repository.mapper.UserMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UsersImpl(private val userRepository: UserRepository, private val passwordEncoder: PasswordEncoder, @Value("\${auth.jwt.secret") private val secret: String) :
    Users {

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

    override fun createUser(email: String, username: String, rawPassword: String): User {
        val userMapper = UserMapper(
            email = email,
            username = username,
            password = passwordEncoder.encode(rawPassword)
        )
        val savedUserMapper = userRepository.save(userMapper)
        return User.from(savedUserMapper)
    }

    override fun checkUserExist(email: String, username: String): UserExist {
        val isEmailExist = userRepository.existsByEmail(email)
        val isUsernameExist = userRepository.existsByUsername(username)
        return UserExist(isEmailExist, isUsernameExist)
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
