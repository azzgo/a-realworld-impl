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
import how.realworld.server.model.createUser
import how.realworld.server.model.Users
import io.mockk.every
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.TestTemplate
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@Provider("realworldServer")
@PactFolder("./../contacts/pacts")
@PactFilter(
        "user exist and get user info",
        "user exists and update user info"
)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class UserProviderTest {
    @LocalServerPort
    private val port = 0

    @BeforeEach
    fun before(context: PactVerificationContext) {
        context.target = HttpTestTarget("localhost", port)
    }

    @MockkBean
    private lateinit var users: Users

    @TestTemplate
    @ExtendWith(PactVerificationSpringProvider::class)
    fun pactVerificationTestTemplate(context: PactVerificationContext) {
        context.verifyInteraction()
    }

    @State("user exist and get user info", action = StateChangeAction.SETUP, comment = "用户存在，获取用户信息")
    fun userExistsAndGetUserInfo() {
        val user = createUser(bio = "I work at statefarm", email = "jake@jake.jake", image = "http://image.url", username = "jake")
        every { users.getById("jake_id") } returns user
        every { users.generateTokenForUser(user) } returns "jwt.token.here"
    }

    @State("user exists and update user info", action = StateChangeAction.SETUP, comment = "用户存在，更新用户信息")
    fun userExistsAndUpdateUserInfo() {
        val user = createUser(bio = "I work at statefarm", email = "jake-john@jake.jake", image = "http://image.url", username = "jake John")
        every {
            users.update(
                userId = "jake_id",
                username = "jake John",
                email = "jake-john@jake.jake",
                bio = "I work at statefarm",
                image = "http://image.url",
                password = "jakejakejake"
            )
        } returns user

        every { users.generateTokenForUser(user) } returns "jwt.token.here"
    }
}