package how.realworld.server.model.impl

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import how.realworld.server.model.User
import how.realworld.server.repository.UserRepository
import how.realworld.server.repository.mapper.UserMapper
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.slot
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.context.ActiveProfiles
import java.util.Optional


@ActiveProfiles("test")
class UsersImplTest {
    private lateinit var userRepository: UserRepository
    private lateinit var passwordEncoder: PasswordEncoder

    @BeforeEach
    fun setup() {
        userRepository = mockk()
        passwordEncoder = mockk()
    }

    @Test
    fun should_get_user_by_email() {
        val userMapper = mockk<UserMapper>()
        every { userMapper.id } returns "jake_id"
        every { userRepository.findByEmail("jake@jake.jake") } returns (userMapper)
        every { userMapper.email } returns ("jake@jake.jake")
        every { userMapper.password } returns "jakejake"
        every { userMapper.username } returns "jake"
        every { userMapper.bio } returns "bio"
        every { userMapper.image } returns "image"

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
        every { userRepository.existsByEmail("jake@jake.jake") } returns false
        every { userRepository.existsByUsername("jake") } returns false
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

        every { userRepository.save(any()) } returns userMapper
        every { passwordEncoder.encode("jakejake") } returns "encodedJakeJake"
        val users = UsersImpl(userRepository, passwordEncoder, "")

        val user = users.create("jake@jake.jake", "jake", "jakejake")

        assertThat(user.userId, equalTo("id"))
        val savedArgumentCaptor = slot<UserMapper>()
        verify { userRepository.save(capture(savedArgumentCaptor)) }
        assertThat(savedArgumentCaptor.captured.password, equalTo("encodedJakeJake"))
    }

    @Test
    fun should_return_user_by_id() {
        val userMapper = UserMapper(
            id = "id",
            email = "jake@jake.jake",
            username = "jake",
            password = "encodedJakeJake"
        )

        every { userRepository.findById("id") } returns Optional.of(userMapper)

        val users = UsersImpl(userRepository, passwordEncoder, "")

        val user = users.getById("id")

        assertThat(user?.userId, equalTo("id"))
        assertThat(user?.email, equalTo("jake@jake.jake"))
        assertThat(user?.username, equalTo("jake"))
        assertThat(user?.password, equalTo("encodedJakeJake"))
    }

    @Test
    fun should_update_user() {
        val userMapper = UserMapper(
            id = "id",
            email = "jake@jake.jake.new",
            username = "jakeNew",
            password = "encodedJakeJakeNew",
            bio = "bioNew",
            image = "http://image.url"
        )
        val oldUserMapper = UserMapper(
            id = "id",
            email = "jake@jake.jake",
            username = "jake",
            password = "encodedJakeJake",
            bio = "bio",
            image = "http://image.url"
        )

        every { userRepository.save(any()) } returns userMapper
        every { userRepository.findById("id") } returns Optional.of(oldUserMapper)
        every { passwordEncoder.encode("jakejakeNew") } returns "encodedJakeJakeNew"
        val users = UsersImpl(userRepository, passwordEncoder, "")

        val user = users.update(
            userId = "id",
            username = "jakeNew",
            email = "jake@jake.jake.new",
            password = "jakejakeNew",
            bio = "bioNew",
        )


        assertThat(user.userId, equalTo("id"))
        assertThat(user.username, equalTo("jakeNew"))
        assertThat(user.bio, equalTo("bioNew"))
        assertThat(user.image, equalTo("http://image.url"))
        assertThat(user.email, equalTo("jake@jake.jake.new"))
        assertThat(user.password, equalTo("encodedJakeJakeNew"))


        val savedArgumentCaptor = slot<UserMapper>()
        verify { userRepository.save(capture(savedArgumentCaptor)) }
        assertThat(savedArgumentCaptor.captured.password, equalTo("encodedJakeJakeNew"))
        assertThat(savedArgumentCaptor.captured.email, equalTo("jake@jake.jake.new"))
        assertThat(savedArgumentCaptor.captured.id, equalTo("id"))
        assertThat(savedArgumentCaptor.captured.username, equalTo("jakeNew"))
        assertThat(savedArgumentCaptor.captured.bio, equalTo("bioNew"))
    }
}
