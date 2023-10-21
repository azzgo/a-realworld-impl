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
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyList
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.jupiter.MockitoExtension
import java.time.Instant
import java.util.*

@ExtendWith(MockitoExtension::class)
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
        MockitoAnnotations.openMocks(this)
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

        // need add a Verity here later
        `when`(articleRepository.findById("slug")).thenReturn(Optional.of(oldArticleMapper))
        `when`(tagRepository.saveOrUpdateAll(anyList())).thenReturn(tags)
        `when`(articleRepository.save(oldArticleMapper)).thenReturn(newArticleMapper)

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
        val user = mock(User::class.java)
        `when`(user.userId).thenReturn("jake_id")
        `when`(user.username).thenReturn("jake")
        `when`(user.bio).thenReturn("I work at statefarm")
        `when`(user.image).thenReturn("http://image.url")
        `when`(users.getById("jake_id")).thenReturn(user)
        val tags = listOf(TagMapper("tag_id_1", "tag1"), TagMapper("tag_id_2", "tag2"))

        val queriedArticle = mock(ArticleMapper::class.java)
        `when`(queriedArticle.id).thenReturn("slug")
        `when`(queriedArticle.title).thenReturn("title")
        `when`(queriedArticle.description).thenReturn("description")
        `when`(queriedArticle.body).thenReturn("body")
        `when`(queriedArticle.tagList).thenReturn(tags)
        `when`(queriedArticle.createdAt).thenReturn(Instant.parse("2016-02-18T03:22:56.637Z"))
        `when`(queriedArticle.updatedAt).thenReturn(Instant.parse("2016-02-18T03:22:56.637Z"))
        `when`(queriedArticle.favorited).thenReturn(false)
        `when`(queriedArticle.favoritesCount).thenReturn(1)
        `when`(queriedArticle.authorId).thenReturn("jake_id")

        `when`(articleRepository.findById("slug")).thenReturn(Optional.of(queriedArticle))

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
}