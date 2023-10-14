package how.realworld.server.repository

import how.realworld.server.repository.mapper.UserMapper
import jakarta.transaction.Transactional
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@AutoConfigureTestEntityManager
@ActiveProfiles("test")
class UserRepositoryTest {
    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var testEntityManager: TestEntityManager

    @Test
    @Transactional
    fun should_get_user_mapper_from_db() {
        testEntityManager.persist(stubUser("jake@jake.jake", "jake"))
        val userMapper = userRepository.findByEmail("jake@jake.jake")
        assertThat(userMapper, notNullValue())
    }

    fun stubUser(email: String, username: String): UserMapper {
        return UserMapper(
                email = email,
                username = username,
                password = "password"
        )
    }
}