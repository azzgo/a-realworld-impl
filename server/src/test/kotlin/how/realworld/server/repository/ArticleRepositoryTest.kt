package how.realworld.server.repository

import how.realworld.server.repository.mapper.ArticleMapper
import how.realworld.server.repository.mapper.TagMapper
import jakarta.transaction.Transactional
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@AutoConfigureTestEntityManager
@ActiveProfiles("test")
class ArticleRepositoryTest {
    @Autowired
    private lateinit var articleRepository: ArticleRepository
    @Autowired
    private lateinit var tagRepository: TagRepository

    @Autowired
    private lateinit var testEntityManager: TestEntityManager

    @Test
    @Transactional
    fun should_save_new_article() {
        val tags = tagRepository.saveAll(listOf(TagMapper(name = "tag1"), TagMapper(name = "tag2")))
        val savedArticleMapper = articleRepository.save(ArticleMapper(
                title = "title",
                description = "description",
                body = "body",
                tagList = tags,
                authorId = "user_id"
        ))

        val dbSavedArticleMapper = testEntityManager.find(ArticleMapper::class.java, savedArticleMapper.id)

        assertThat(dbSavedArticleMapper, notNullValue())
        assertThat(dbSavedArticleMapper.title, equalTo("title"))
        assertThat(dbSavedArticleMapper.description, equalTo("description"))
        assertThat(dbSavedArticleMapper.body, equalTo("body"))
        assertThat(dbSavedArticleMapper.tagList.map { it.name }, equalTo(listOf("tag1", "tag2")))
        assertThat(dbSavedArticleMapper.authorId, equalTo("user_id"))
    }
}