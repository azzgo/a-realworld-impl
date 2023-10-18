package how.realworld.server.model.impl

import how.realworld.server.model.User
import how.realworld.server.model.Users
import how.realworld.server.repository.ArticleRepository
import how.realworld.server.repository.mapper.ArticleMapper
import how.realworld.server.repository.mapper.TagMapper
import org.junit.jupiter.api.Test

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.mockito.ArgumentMatchers.any
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import java.time.Instant

class ArticlesImplTest {

    @InjectMocks
    private lateinit var articlesImpl: ArticlesImpl

    @Mock
    private lateinit var users: Users
    @Mock
    private lateinit var articleRepository: ArticleRepository

    @BeforeEach
    fun setup() {
        users = mock(Users::class.java)
        articleRepository = mock(ArticleRepository::class.java)
        articlesImpl = ArticlesImpl(users, articleRepository)
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
        `when`(articleMapper.id).thenReturn("slug_id")
        `when`(articleMapper.title).thenReturn("title")
        `when`(articleMapper.description).thenReturn("description")
        `when`(articleMapper.body).thenReturn("body")
        `when`(articleMapper.tagList).thenReturn(listOf(TagMapper("tag_id_1", "tag1"), TagMapper("tag_id_2", "tag2")))
        `when`(articleMapper.createdAt).thenReturn(Instant.parse("2016-02-18T03:22:56.637Z"))
        `when`(articleMapper.updatedAt).thenReturn(Instant.parse("2016-02-18T03:22:56.637Z"))
        `when`(articleMapper.favorited).thenReturn(false)
        `when`(articleMapper.favoritesCount).thenReturn(0)
        `when`(articleMapper.authorId).thenReturn("jake_id")

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
}