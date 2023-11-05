package how.realworld.server.pact.provider

import au.com.dius.pact.provider.junit5.HttpTestTarget
import au.com.dius.pact.provider.junit5.PactVerificationContext
import au.com.dius.pact.provider.junitsupport.Provider
import au.com.dius.pact.provider.junitsupport.State
import au.com.dius.pact.provider.junitsupport.StateChangeAction
import au.com.dius.pact.provider.junitsupport.loader.PactFilter
import au.com.dius.pact.provider.junitsupport.loader.PactFolder
import au.com.dius.pact.provider.spring.junit5.PactVerificationSpringProvider
import com.ninjasquad.springmockk.MockkBean
import how.realworld.server.model.User
import how.realworld.server.model.UserExist
import how.realworld.server.model.Users
import how.realworld.server.repository.BaseRepositoryTest
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.TestTemplate
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
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
class UsersAuthProviderTest: BaseRepositoryTest() {
    @MockkBean
    private lateinit var users: Users

    @MockkBean
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
        val user = mockk<User>()
        every { users.getByEmail("jake@jake.jake") } returns user
        every { passwordEncoder.matches(eq("jakejake"), any()) } returns true
        every { user.email } returns "jake@jake.jake"
        every { user.password } returns ""
        every { user.username } returns "jake"
        every { user.bio } returns "I work at statefarm"
        every { user.image } returns "http://image.url"
        every { users.generateTokenForUser(user) } returns jwtTestToken
    }

    @State("user not or password invalid", comment = "用户不存在，或密码错误")
    fun userDoesNotExistOrPasswordInvalid() {
        val user = mockk<User>()
        every { users.getByEmail("fake@jake.jake") } returns user
        every { user.password } returns "fakefake"
        every { passwordEncoder.matches(eq("fakefake"), any()) } returns false
    }

    @State("user not registered", comment = "用户未注册")
    fun userNotRegistered() {
        val user = User(
            email = "jake@jake.jake",
            password = "",
            username = "jake",
        )
        every { users.checkUserExist("jake@jake.jake", "jake") } returns UserExist(email = false, username = false)
        every { users.create("jake@jake.jake", "jake", "jakejake") } returns user
        every { users.generateTokenForUser(user) } returns jwtTestToken
    }

    @State("email already exist when registering", comment = "注册时邮箱已存在")
    fun emailAlreadyExistWhenRegistering() {
        every { users.checkUserExist("jake@jake.taken", "jake") } returns UserExist(email = true, username = false)
    }

    @State("username already exist when registering", comment = "注册时用户名已存在")
    fun usernameAlreadyExistWhenRegistering() {
        every { users.checkUserExist("jake@jake.jake", "jake_exist") } returns UserExist(email = false, username = true)
    }

    @State("both email and username already exist when registering", comment = "注册时邮箱和用户名都已存在")
    fun bothEmailAndUsernameAlreadyExistWhenRegistering() {
        every { users.checkUserExist("jake@jake.taken", "jake_exist") } returns UserExist(email = true, username = true)
    }
}
