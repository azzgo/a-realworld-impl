package how.realworld.server.repository

import how.realworld.server.repository.mapper.UserMapper
import jakarta.transaction.Transactional
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.CoreMatchers.equalTo
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

    @Test
    @Transactional
    fun should_update_user_mapper_in_db() {
        val entity= testEntityManager.persist(stubUser("jake@jake.jake", "jake"))
        val userMapper = userRepository.save(UserMapper(
                id = entity.id,
                email = "jake@jake.jake.new",
                username = "jakeNew",
                password = "jakejakeNew",
                bio = "bioNew",
                image = "http://image.url"
        ))

        assertThat(userMapper.email, equalTo("jake@jake.jake.new"))
        assertThat(userMapper.username, equalTo("jakeNew"))
        assertThat(userMapper.bio, equalTo("bioNew"))
        assertThat(userMapper.image, equalTo("http://image.url"))
        assertThat(userMapper.password, equalTo("jakejakeNew"))
    }

    @Test
    @Transactional
    fun should_email_exist() {
        testEntityManager.persist(stubUser("jake@jake.jake", "jake"))
        val existsByEmail = userRepository.existsByEmail("jake@jake.jake")
        assertThat(existsByEmail, equalTo(true))
    }

    @Test
    @Transactional
    fun should_username_exist() {
        testEntityManager.persist(stubUser("jake@jake.jake", "jake"))
        val userExist = userRepository.existsByUsername("jake")
        assertThat(userExist, equalTo(true))
    }

    @Test
    @Transactional
    fun should_save_new_user() {
        val savedUserMapper = userRepository.save(UserMapper(
            email = "jake@jake.jake",
            username = "jake",
            password = "encodedPassword"
        ))

        val dbSavedUserMapper = testEntityManager.find(UserMapper::class.java, savedUserMapper.id)

        assertThat(dbSavedUserMapper, notNullValue())
        assertThat(dbSavedUserMapper.email, equalTo("jake@jake.jake"))
        assertThat(dbSavedUserMapper.username, equalTo("jake"))
        assertThat(dbSavedUserMapper.password, equalTo("encodedPassword"))
    }

    fun stubUser(email: String, username: String): UserMapper {
        return UserMapper(
            email = email,
            username = username,
            password = "password"
        )
    }
}