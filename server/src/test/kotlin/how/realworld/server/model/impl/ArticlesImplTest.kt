package how.realworld.server.model.impl

import how.realworld.server.model.User
import how.realworld.server.model.Users
import how.realworld.server.repository.ArticleRepository
import how.realworld.server.repository.TagRepository
import how.realworld.server.repository.mapper.ArticleMapper
import how.realworld.server.repository.mapper.TagMapper
import how.realworld.server.repository.saveOrUpdateAll
import org.junit.jupiter.api.Test

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyList
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import java.time.Instant
import java.util.*

class ArticlesImplTest {

    @InjectMocks
    private lateinit var articlesImpl: ArticlesImpl

    @Mock
    private lateinit var users: Users

    @Mock
    private lateinit var articleRepository: ArticleRepository

    @Mock
    private lateinit var tagRepository: TagRepository

    @BeforeEach
    fun setup() {
        users = mock(Users::class.java)
        articleRepository = mock(ArticleRepository::class.java)
        tagRepository = mock(TagRepository::class.java)
        articlesImpl = ArticlesImpl(users, articleRepository, tagRepository)
    }

    @Test
    fun should_create_new_article() {
        val user = mock(User::class.java)
        `when`(user.userId).thenReturn("jake_id")
        `when`(user.username).thenReturn("jake")
        `when`(user.bio).thenReturn("I work at statefarm")
        `when`(user.image).thenReturn("http://image.url")
        `when`(users.getById("jake_id")).thenReturn(user)

        val articleMapper = mock(ArticleMapper::class.java)
        val tags = listOf(TagMapper("tag_id_1", "tag1"), TagMapper("tag_id_2", "tag2"))
        `when`(articleMapper.id).thenReturn("slug_id")
        `when`(articleMapper.title).thenReturn("title")
        `when`(articleMapper.description).thenReturn("description")
        `when`(articleMapper.body).thenReturn("body")
        `when`(articleMapper.tagList).thenReturn(tags)
        `when`(articleMapper.createdAt).thenReturn(Instant.parse("2016-02-18T03:22:56.637Z"))
        `when`(articleMapper.updatedAt).thenReturn(Instant.parse("2016-02-18T03:22:56.637Z"))
        `when`(articleMapper.favorited).thenReturn(false)
        `when`(articleMapper.favoritesCount).thenReturn(0)
        `when`(articleMapper.authorId).thenReturn("jake_id")

        // need add a Verity here later
        `when`(tagRepository.saveOrUpdateAll(anyList())).thenReturn(tags)
        `when`(articleRepository.save(any())).thenReturn(articleMapper)

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
        val user = mock(User::class.java)
        `when`(user.userId).thenReturn("jake_id")
        `when`(user.username).thenReturn("jake")
        `when`(user.bio).thenReturn("I work at statefarm")
        `when`(user.image).thenReturn("http://image.url")
        `when`(users.getById("jake_id")).thenReturn(user)

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

        val newArticleMapper = mock(ArticleMapper::class.java)
        `when`(newArticleMapper.id).thenReturn("slug")
        `when`(newArticleMapper.title).thenReturn("title")
        `when`(newArticleMapper.description).thenReturn("description")
        `when`(newArticleMapper.body).thenReturn("body")
        `when`(newArticleMapper.tagList).thenReturn(tags)
        `when`(newArticleMapper.createdAt).thenReturn(Instant.parse("2016-02-18T03:22:56.637Z"))
        `when`(newArticleMapper.updatedAt).thenReturn(Instant.parse("2016-02-18T03:22:56.637Z"))
        `when`(newArticleMapper.favorited).thenReturn(false)
        `when`(newArticleMapper.favoritesCount).thenReturn(1)
        `when`(newArticleMapper.authorId).thenReturn("jake_id")

        // need add a Verity here later
        `when`(articleRepository.findById("slug")).thenReturn(Optional.of(oldArticleMapper))
        `when`(tagRepository.saveOrUpdateAll(anyList())).thenReturn(tags)
        `when`(articleRepository.save(oldArticleMapper)).thenReturn(newArticleMapper)

        val updateedArticle = articlesImpl.update("slug", "jake_id", "title", "description", "body", listOf("tag1", "tag2"))

        // verify that oldArticleMapper title, body and description, tagList are upadted by update function parameter
        assertThat(oldArticleMapper.title).isEqualTo("title")
        assertThat(oldArticleMapper.description).isEqualTo("description")
        assertThat(oldArticleMapper.body).isEqualTo("body")
        assertThat(oldArticleMapper.tagList.map { it.name }).isEqualTo(listOf("tag1", "tag2"))

       // verify return value is correct
        assertThat(updateedArticle.slug).isEqualTo("slug")
        assertThat(updateedArticle.title).isEqualTo("title")
        assertThat(updateedArticle.description).isEqualTo("description")
        assertThat(updateedArticle.body).isEqualTo("body")
        assertThat(updateedArticle.tagList.map { it.name }).isEqualTo(listOf("tag1", "tag2"))
        assertThat(updateedArticle.author.username).isEqualTo("jake")
        assertThat(updateedArticle.createdAt).isEqualTo(Instant.parse("2016-02-18T03:22:56.637Z"))
        assertThat(updateedArticle.updatedAt).isEqualTo(Instant.parse("2016-02-18T03:22:56.637Z"))
        assertThat(updateedArticle.favorited).isFalse()
        assertThat(updateedArticle.favoritesCount).isEqualTo(1)
        assertThat(updateedArticle.author.bio).isEqualTo("I work at statefarm")
        assertThat(updateedArticle.author.image).isEqualTo("http://image.url")
        assertThat(updateedArticle.author.following).isFalse()
    }
}