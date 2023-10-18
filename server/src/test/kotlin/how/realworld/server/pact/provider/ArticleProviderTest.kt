package how.realworld.server.pact.provider

import au.com.dius.pact.provider.junit5.HttpTestTarget
import au.com.dius.pact.provider.junit5.PactVerificationContext
import au.com.dius.pact.provider.junitsupport.Provider
import au.com.dius.pact.provider.junitsupport.State
import au.com.dius.pact.provider.junitsupport.StateChangeAction
import au.com.dius.pact.provider.junitsupport.loader.PactFilter
import au.com.dius.pact.provider.junitsupport.loader.PactFolder
import au.com.dius.pact.provider.spring.junit5.PactVerificationSpringProvider
import how.realworld.server.model.Articles
import how.realworld.server.model.createArticle
import how.realworld.server.model.createAuthor
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.TestTemplate
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.`when`
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.time.Instant

@ExtendWith(SpringExtension::class)
@Provider("realworldServer")
@PactFolder("./../contacts/pacts")
@PactFilter(
        "user logged in what to post new article",
)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class ArticleProviderTest {
    @LocalServerPort
    private val port = 0

    @BeforeEach
    fun before(context: PactVerificationContext) {
        context.target = HttpTestTarget("localhost", port)
    }

    @MockBean
    private lateinit var articles: Articles

    @TestTemplate
    @ExtendWith(PactVerificationSpringProvider::class)
    fun pactVerificationTestTemplate(context: PactVerificationContext) {
        context.verifyInteraction()
    }

    @State("user logged in what to post new article", action = StateChangeAction.SETUP, comment = "用户登录，发表新文章")
    fun userLoggedInWhenToPostNewArticle() {
        val createdArticle = createArticle(
                slug = "how-to-train-your-dragon",
                title = "How to train your dragon",
                description = "Ever wonder how?",
                body = "You have to believe",
                tagList = listOf("reactjs", "angularjs", "dragons"),
                createdAt = Instant.parse("2016-02-18T03:22:56.637Z"),
                updatedAt = Instant.parse("2016-02-18T03:48:35.824Z"),
                favorited = false,
                favoritesCount = 0,
                author = createAuthor(
                        username = "jake",
                        bio = "I work at statefarm",
                        image = "http://image.url",
                        following = false,
                )
        )
        `when`(articles.create(userId = "jake_id",
                title = createdArticle.title,
                description = createdArticle.description,
                body = createdArticle.body,
                tagList = createdArticle.tagList)).thenReturn(createdArticle)
    }
}