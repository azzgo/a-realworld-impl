package how.realworld.server.model.impl

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import how.realworld.server.model.User
import how.realworld.server.repository.UserRepository
import how.realworld.server.repository.mapper.UserMapper
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentCaptor
import org.mockito.Mockito.*
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.context.ActiveProfiles
import java.util.Optional


@ActiveProfiles("test")
class UsersImplTest {
    private lateinit var userRepository: UserRepository
    private lateinit var passwordEncoder: PasswordEncoder

    @BeforeEach
    fun setup() {
        userRepository = mock(UserRepository::class.java)
        passwordEncoder = mock(BCryptPasswordEncoder::class.java)
    }

    @Test
    fun should_get_user_by_email() {
        val userMapper = mock(UserMapper::class.java)
        `when`(userRepository.findByEmail("jake@jake.jake")).thenReturn(userMapper)
        `when`(userMapper.email).thenReturn("jake@jake.jake")
        `when`(userMapper.password).thenReturn("jakejake")
        `when`(userMapper.username).thenReturn("jake")
        `when`(userMapper.bio).thenReturn("bio")
        `when`(userMapper.image).thenReturn("image")

        val users = UsersImpl(userRepository, passwordEncoder, "")

        val byEmailUser = users.getByEmail("jake@jake.jake")

        assertThat(byEmailUser?.email, equalTo("jake@jake.jake"))
        assertThat(byEmailUser?.username, equalTo("jake"))
        assertThat(byEmailUser?.password, equalTo("jakejake"))
        assertThat(byEmailUser?.bio, equalTo("bio"))
        assertThat(byEmailUser?.image, equalTo("image"))
    }

    @Test
    fun should_get_token_from_user() {
        val user =
                User(
                        userId = "id",
                        email = "jake@jake.jake",
                        password = "jakejake",
                        username = "jake",
                        bio = "bio",
                        image = "image"
                )

        val secret =
                "a625cbe1-b347-4c56-a98c-f6678b9ae0a4"
        val users = UsersImpl(userRepository, passwordEncoder, secret)

        val token = users.generateTokenForUser(user)

        val verifier: JWTVerifier = JWT.require(Algorithm.HMAC256(secret))
                .build()

        verifier.verify(token)
    }

    @Test
    fun should_return_no_user_exists_status() {
        `when`(userRepository.existsByEmail("jake.jake.jake")).thenReturn(false)
        `when`(userRepository.existsByUsername("jake")).thenReturn(false)
        val users = UsersImpl(userRepository, passwordEncoder, "")
        val userExist = users.checkUserExist("jake@jake.jake", "jake")

        assertThat(userExist.email, equalTo(false))
        assertThat(userExist.username, equalTo(false))
    }

    @Test
    fun should_createUser() {
        val userMapper = UserMapper(
                id = "id",
                email = "jake@jake.jake",
                username = "jake",
                password = "encodedJakeJake"
        )

        `when`(userRepository.save(any())).thenReturn(userMapper)
        `when`(passwordEncoder.encode("jakejake")).thenReturn("encodedJakeJake")
        val users = UsersImpl(userRepository, passwordEncoder, "")

        val user = users.create("jake@jake.jake", "jake", "jakejake")

        assertThat(user.userId, equalTo("id"))
        val savedArgumentCaptor = ArgumentCaptor.forClass(UserMapper::class.java)
        verify(userRepository).save(savedArgumentCaptor.capture())
        assertThat(savedArgumentCaptor.value.password, equalTo("encodedJakeJake"))
    }

    @Test
    fun should_return_user_by_id() {
        val userMapper = UserMapper(
                id = "id",
                email = "jake@jake.jake",
                username = "jake",
                password = "encodedJakeJake"
        )

        `when`(userRepository.findById("id")).thenReturn(Optional.of(userMapper))

        val users = UsersImpl(userRepository, passwordEncoder, "")

        val user = users.getById("id")

        assertThat(user?.userId, equalTo("id"))
        assertThat(user?.email, equalTo("jake@jake.jake"))
        assertThat(user?.username, equalTo("jake"))
        assertThat(user?.password, equalTo("encodedJakeJake"))
    }
}
