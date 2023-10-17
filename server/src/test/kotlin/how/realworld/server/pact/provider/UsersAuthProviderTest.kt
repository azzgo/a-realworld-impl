package how.realworld.server.pact.provider

import au.com.dius.pact.provider.junit5.HttpTestTarget
import au.com.dius.pact.provider.junit5.PactVerificationContext
import au.com.dius.pact.provider.junitsupport.Provider
import au.com.dius.pact.provider.junitsupport.State
import au.com.dius.pact.provider.junitsupport.StateChangeAction
import au.com.dius.pact.provider.junitsupport.loader.PactFilter
import au.com.dius.pact.provider.junitsupport.loader.PactFolder
import au.com.dius.pact.provider.spring.junit5.PactVerificationSpringProvider
import how.realworld.server.model.User
import how.realworld.server.model.UserExist
import how.realworld.server.model.Users
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.TestTemplate
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.*
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.context.junit.jupiter.SpringExtension


@ExtendWith(SpringExtension::class)
@Provider("realworldServer")
@PactFolder("./../contacts/pacts")
@PactFilter(
    "user exists",
    "user not or password invalid",
    "user not registered",
    "email already exist when registering",
    "username already exist when registering",
    "both email and username already exist when registering"
)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UsersAuthProviderTest {
    @MockBean
    private lateinit var users: Users

    @MockBean
    private lateinit var passwordEncoder: PasswordEncoder

    @LocalServerPort
    private val port = 0

    @BeforeEach
    fun before(context: PactVerificationContext) {
        context.target = HttpTestTarget("localhost", port)
    }

    @TestTemplate
    @ExtendWith(PactVerificationSpringProvider::class)
    fun pactVerificationTestTemplate(context: PactVerificationContext) {
        context.verifyInteraction()
    }

    @State("user exists", action = StateChangeAction.SETUP, comment = "用户存在")
    fun userExists() {
        val user = mock(User::class.java)
        `when`(users.getByEmail("jake@jake.jake")).thenReturn(user)
        `when`(passwordEncoder.matches(eq("jakejake"), any())).thenReturn(true)
        `when`(user.email).thenReturn("jake@jake.jake")
        `when`(user.password).thenReturn("")
        `when`(user.username).thenReturn("jake")
        `when`(user.bio).thenReturn("I work at statefarm")
        `when`(user.image).thenReturn("http://image.url")
        `when`(users.generateTokenForUser(user)).thenReturn("jwt.token.here")
    }

    @State("user not or password invalid", comment = "用户不存在，或密码错误")
    fun userDoesNotExistOrPasswordInvalid() {
        val user = mock(User::class.java)
        `when`(users.getByEmail("fake@jake.jake")).thenReturn(user)
        `when`(passwordEncoder.matches(eq("fakefake"), any())).thenReturn(false)
    }

    @State("user not registered", comment = "用户未注册")
    fun userNotRegistered() {
        val user = User(
            email = "jake@jake.jake",
            password = "",
            username = "jake",
        )
        `when`(users.checkUserExist("jake@jake.jake", "jake")).thenReturn(UserExist(email = false, username = false))
        `when`(users.create("jake@jake.jake", "jake", "jakejake")).thenReturn(
            user
        )
        `when`(users.generateTokenForUser(user)).thenReturn("jwt.token.here")
    }

    @State("email already exist when registering", comment = "注册时邮箱已存在")
    fun emailAlreadyExistWhenRegistering() {
        `when`(users.checkUserExist("jake@jake.taken", "jake")).thenReturn(UserExist(email = true, username = false))
    }

    @State("username already exist when registering", comment = "注册时用户名已存在")
    fun usernameAlreadyExistWhenRegistering() {
        `when`(users.checkUserExist("jake@jake.jake", "jake_exist")).thenReturn(
            UserExist(
                email = false,
                username = true
            )
        )
    }

    @State("both email and username already exist when registering", comment = "注册时邮箱和用户名都已存在")
    fun bothEmailAndUsernameAlreadyExistWhenRegistering() {
        `when`(users.checkUserExist("jake@jake.taken", "jake_exist")).thenReturn(
            UserExist(
                email = true,
                username = true
            )
        )
    }
}