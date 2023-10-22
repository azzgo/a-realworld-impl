package how.realworld.server.model.impl

import how.realworld.server.model.*
import how.realworld.server.repository.*
import how.realworld.server.repository.mapper.ArticleMapper
import how.realworld.server.repository.mapper.TagMapper
import how.realworld.server.repository.mapper.UserMapper
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import org.junit.jupiter.api.Test

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import java.time.Instant
import java.util.*

class ArticlesImplTest {
    private lateinit var articlesImpl: ArticlesImpl
    private lateinit var userRepository: UserRepository
    private lateinit var articleRepository: ArticleRepository
    private lateinit var tagRepository: TagRepository

    @BeforeEach
    fun setup() {
        userRepository = mockk()
        articleRepository = mockk()
        tagRepository = mockk()
        articlesImpl = ArticlesImpl(userRepository, articleRepository, tagRepository)
    }

    @Test
    fun should_create_new_article() {
        val userMapper = UserMapper(
            id = "jake_id",
            username = "jake",
            email = "jake@jake.jake",
            bio = "I work at statefarm",
            image = "http://image.url",
            password = "password"
        )
        every { userRepository.findById("jake_id") } returns Optional.of(userMapper)

        val articleMapper = mockk<ArticleMapper>()
        val tags = listOf(TagMapper("tag_id_1", "tag1"), TagMapper("tag_id_2", "tag2"))
        every { articleMapper.id } returns ("slug_id")
        every { articleMapper.title } returns ("title")
        every { articleMapper.description } returns ("description")
        every { articleMapper.body } returns ("body")
        every { articleMapper.tagList } returns (tags)
        every { articleMapper.createdAt } returns (Instant.parse("2016-02-18T03:22:56.637Z"))
        every { articleMapper.updatedAt } returns (Instant.parse("2016-02-18T03:22:56.637Z"))
        every { articleMapper.favorited } returns (false)
        every { articleMapper.favoritesCount } returns (0)

        // need add a Verity here later
        every { tagRepository.saveOrUpdateAll(any()) } returns (tags)
        every { articleRepository.save(any()) } returns (articleMapper)

        val createdArticle = articlesImpl.create("jake_id", "title", "description", "body", listOf("tag1", "tag2"))

        assertThat(createdArticle.slug).isEqualTo("slug_id")
        assertThat(createdArticle.title).isEqualTo("title")
        assertThat(createdArticle.description).isEqualTo("description")
        assertThat(createdArticle.body).isEqualTo("body")
        assertThat(createdArticle.tagList.map { it.name }).isEqualTo(listOf("tag1", "tag2"))
        assertThat(createdArticle.author.username).isEqualTo("jake")
        assertThat(createdArticle.createdAt).isEqualTo(Instant.parse("2016-02-18T03:22:56.637Z"))
        assertThat(createdArticle.updatedAt).isEqualTo(Instant.parse("2016-02-18T03:22:56.637Z"))
        assertThat(createdArticle.favorited).isFalse()
        assertThat(createdArticle.favoritesCount).isEqualTo(0)
        assertThat(createdArticle.author.bio).isEqualTo("I work at statefarm")
        assertThat(createdArticle.author.image).isEqualTo("http://image.url")
        assertThat(createdArticle.author.following).isFalse()
    }

    @Test
    fun should_update_article() {
        val userMapper = UserMapper(
            id = "jake_id",
            username = "jake",
            email = "jake@jake.jake",
            bio = "I work at statefarm",
            image = "http://image.url",
            password = "password"
        )
        every { userRepository.findById("jake_id") } returns (Optional.of(userMapper))

        val oldArticleMapper = ArticleMapper(
            id = "slug",
            title = "old_title",
            description = "old_description",
            body = "old_body",
            tagList = listOf(TagMapper("tag_id_1", "tag1"), TagMapper("tag_id_2", "tag2")),
            createdAt = Instant.parse("2016-02-18T03:22:56.637Z"),
            updatedAt = Instant.parse("2016-02-18T03:22:56.637Z"),
            favorited = false,
            favoritesCount = 0,
            authorId = "jake_id"
        )
        val tags = listOf(TagMapper("tag_id_1", "tag1"), TagMapper("tag_id_2", "tag2"))

        val newArticleMapper = mockk<ArticleMapper>()
        every { newArticleMapper.id } returns ("slug")
        every { newArticleMapper.title } returns ("title")
        every { newArticleMapper.description } returns ("description")
        every { newArticleMapper.body } returns ("body")
        every { newArticleMapper.tagList } returns (tags)
        every { newArticleMapper.createdAt } returns (Instant.parse("2016-02-18T03:22:56.637Z"))
        every { newArticleMapper.updatedAt } returns (Instant.parse("2016-02-18T03:22:56.637Z"))
        every { newArticleMapper.favorited } returns (false)
        every { newArticleMapper.favoritesCount } returns (1)

        // need add a Verity here later
        every { articleRepository.findById("slug") } returns (Optional.of(oldArticleMapper))
        every { tagRepository.saveOrUpdateAll(any()) } returns (tags)
        every { articleRepository.save(oldArticleMapper) } returns (newArticleMapper)

        val updatedArticle =
            articlesImpl.update("slug", "jake_id", "title", "description", "body", listOf("tag1", "tag2"))

        // verify that oldArticleMapper title, body and description, tagList are updated by update function parameter
        assertThat(oldArticleMapper.title).isEqualTo("title")
        assertThat(oldArticleMapper.description).isEqualTo("description")
        assertThat(oldArticleMapper.body).isEqualTo("body")
        assertThat(oldArticleMapper.tagList.map { it.name }).isEqualTo(listOf("tag1", "tag2"))

        // verify return value is correct
        assertThat(updatedArticle.slug).isEqualTo("slug")
        assertThat(updatedArticle.title).isEqualTo("title")
        assertThat(updatedArticle.description).isEqualTo("description")
        assertThat(updatedArticle.body).isEqualTo("body")
        assertThat(updatedArticle.tagList.map { it.name }).isEqualTo(listOf("tag1", "tag2"))
        assertThat(updatedArticle.author.username).isEqualTo("jake")
        assertThat(updatedArticle.createdAt).isEqualTo(Instant.parse("2016-02-18T03:22:56.637Z"))
        assertThat(updatedArticle.updatedAt).isEqualTo(Instant.parse("2016-02-18T03:22:56.637Z"))
        assertThat(updatedArticle.favorited).isFalse()
        assertThat(updatedArticle.favoritesCount).isEqualTo(1)
        assertThat(updatedArticle.author.bio).isEqualTo("I work at statefarm")
        assertThat(updatedArticle.author.image).isEqualTo("http://image.url")
        assertThat(updatedArticle.author.following).isFalse()
    }

    @Test
    fun should_get_article_by_slug() {
        val userMapper = UserMapper(
            id = "jake_id",
            username = "jake",
            email = "jake@jake.jake",
            bio = "I work at statefarm",
            image = "http://image.url",
            password = "password"
        )
        every { userRepository.findById("jake_id") } returns (Optional.of(userMapper))
        val tags = listOf(TagMapper("tag_id_1", "tag1"), TagMapper("tag_id_2", "tag2"))

        val queriedArticle = mockk<ArticleMapper>()
        every { queriedArticle.id } returns ("slug")
        every { queriedArticle.title } returns ("title")
        every { queriedArticle.description } returns ("description")
        every { queriedArticle.body } returns ("body")
        every { queriedArticle.tagList } returns (tags)
        every { queriedArticle.createdAt } returns (Instant.parse("2016-02-18T03:22:56.637Z"))
        every { queriedArticle.updatedAt } returns (Instant.parse("2016-02-18T03:22:56.637Z"))
        every { queriedArticle.favorited } returns (false)
        every { queriedArticle.favoritesCount } returns (1)
        every { queriedArticle.authorId } returns ("jake_id")

        every { articleRepository.findById("slug") } returns (Optional.of(queriedArticle))

        val article = articlesImpl.get("slug")

        assertThat(article?.slug).isEqualTo("slug")
        assertThat(article?.title).isEqualTo("title")
        assertThat(article?.description).isEqualTo("description")
        assertThat(article?.body).isEqualTo("body")
        assertThat(article?.tagList?.map { it.name }).isEqualTo(listOf("tag1", "tag2"))
        assertThat(article?.author?.username).isEqualTo("jake")
        assertThat(article?.createdAt).isEqualTo(Instant.parse("2016-02-18T03:22:56.637Z"))
        assertThat(article?.updatedAt).isEqualTo(Instant.parse("2016-02-18T03:22:56.637Z"))
        assertThat(article?.favorited).isFalse()
        assertThat(article?.favoritesCount).isEqualTo(1)
        assertThat(article?.author?.bio).isEqualTo("I work at statefarm")
        assertThat(article?.author?.image).isEqualTo("http://image.url")
        assertThat(article?.author?.following).isFalse()
    }

    @ParameterizedTest
    @CsvSource("author,jake_id,", ",,tagName", ",,")
    fun should_list_article_by_pagination(authorName: String?, authorId: String?, tagName: String?) {
        mockkStatic("how.realworld.server.repository.ArticleRepositoryKt")
        every {
            articleRepository.queryList(
                tagName = tagName,
                authorId = authorId,
                pageable = PageRequest.of(0, 3, Sort.by("createdAt").descending())
            )
        } returns PageImpl(
            listOf(
                articleAdipiscingElit.toMapper(),
                articleLorem1.toMapper()
            ),
            PageRequest.of(0, 10),
            2
        )
        every {
            userRepository.findByIds(
                listOf(
                    articleAdipiscingElit.author.userId,
                    articleLorem1.author.userId,
                )
            )
        } returns (
                listOf(
                    createUser(
                        userId = articleAdipiscingElit.author.userId,
                        username = articleAdipiscingElit.author.username,
                        bio = articleAdipiscingElit.author.bio,
                        image = articleAdipiscingElit.author.image,
                    ).toMapper(),
                    createUser(
                        userId = articleLorem1.author.userId,
                        username = articleLorem1.author.username,
                        bio = articleLorem1.author.bio,
                        image = articleLorem1.author.image,
                    ).toMapper()
                )
                )

        if (authorName != null && authorId != null) {
            every { userRepository.findByUsername(authorName) } returns createUser(
                userId = authorId,
                username = authorName,
            ).toMapper()
        }


        val page = articlesImpl.list(offset = 0, limit = 3, author = authorName, tag = tagName)

        assertThat(page.size).isEqualTo(10)
        assertThat(page.number).isEqualTo(0)

        assertThat(page.toList().size).isEqualTo(2)
        assertThat(page.toList().first().slug).isEqualTo(articleAdipiscingElit.slug)
        assertThat(page.toList().last().slug).isEqualTo(articleLorem1.slug)
    }
}