package how.realworld.server.repository

import how.realworld.server.model.UserId
import how.realworld.server.repository.mapper.UserMapper
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<UserMapper, UserId> {
    fun findByEmail(email: String): UserMapper?
    fun existsByEmail(email: String): Boolean
    fun existsByUsername(username: String): Boolean
}
