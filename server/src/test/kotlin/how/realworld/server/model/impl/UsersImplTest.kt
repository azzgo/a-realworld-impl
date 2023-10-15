package how.realworld.server.model.impl

import how.realworld.server.model.User
import how.realworld.server.repository.UserRepository
import how.realworld.server.repository.mapper.UserMapper
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles("test")
class UsersImplTest {
    private lateinit var userRepository: UserRepository

    @BeforeEach
    fun setup() {
        userRepository = mock(UserRepository::class.java)
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

        val users = UsersImpl(userRepository, "")

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

        val users = UsersImpl(userRepository, "a625cbe1-b347-4c56-a98c-f6678b9ae0a4")
        val token = users.generateTokenForUser(user)

        assertThat(token, equalTo("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjoiaWQiLCJ1c2VybmFtZSI6Impha2UiLCJlbWFpbCI6Impha2UifQ.xs_1wdAD6LpKwL67Y3OrvJf4X2ZRZl9vw0X3gHnrUOk"))
    }

    @Test
    fun should_return_no_user_exists_status() {
        `when`(userRepository.existsByEmail("jake.jake.jake")).thenReturn(false)
        `when`(userRepository.existsByUsername("jake")).thenReturn(false)
        val users = UsersImpl(userRepository, "")
        val userExist = users.checkUserExist("jake@jake.jake", "jake")

        assertThat(userExist.email, equalTo(false))
        assertThat(userExist.username, equalTo(false))
    }
}
