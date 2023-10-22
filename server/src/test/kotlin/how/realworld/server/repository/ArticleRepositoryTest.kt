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
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.test.context.ActiveProfiles
import java.time.Instant

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
        val savedArticleMapper = articleRepository.save(
            ArticleMapper(
                title = "title",
                description = "description",
                body = "body",
                tagList = tags,
                authorId = "user_id"
            )
        )

        val dbSavedArticleMapper = testEntityManager.find(ArticleMapper::class.java, savedArticleMapper.id)

        assertThat(dbSavedArticleMapper, notNullValue())
        assertThat(dbSavedArticleMapper.title, equalTo("title"))
        assertThat(dbSavedArticleMapper.description, equalTo("description"))
        assertThat(dbSavedArticleMapper.body, equalTo("body"))
        assertThat(dbSavedArticleMapper.tagList.map { it.name }, equalTo(listOf("tag1", "tag2")))
        assertThat(dbSavedArticleMapper.authorId, equalTo("user_id"))
    }

    @Test
    @Transactional
    fun should_query_pagination_articles() {
        val tags = tagRepository.saveAll(listOf(TagMapper(name = "tag1"), TagMapper(name = "tag2")))
        val savedArticleList = articleRepository.saveAll(
            listOf(
                ArticleMapper(
                    title = "title1",
                    description = "description1",
                    body = "body1",
                    tagList = tags,
                    authorId = "user_id",
                    createdAt = Instant.parse("2016-02-18T03:22:56.637Z"),
                ),
                ArticleMapper(
                    title = "title2",
                    description = "description2",
                    body = "body2",
                    tagList = tags,
                    authorId = "user_id",
                    createdAt = Instant.parse("2017-02-18T03:22:56.637Z"),
                ),
                ArticleMapper(
                    title = "title3",
                    description = "description3",
                    body = "body3",
                    tagList = tags,
                    authorId = "user_id",
                    createdAt = Instant.parse("2018-02-18T03:22:56.637Z"),
                ),
                ArticleMapper(
                    title = "title4",
                    description = "description4",
                    body = "body4",
                    tagList = tags,
                    authorId = "user_id",
                    createdAt = Instant.parse("2019-02-18T03:22:56.637Z"),
                )
            )
        )

        val page = articleRepository.findAll(PageRequest.of(0, 3, Sort.by("createdAt").descending()))

        assertThat(page.totalElements, equalTo(4))
        assertThat(
            page.toList().map { it.id }, equalTo(
                listOf(
                    savedArticleList[3].id,
                    savedArticleList[2].id,
                    savedArticleList[1].id,
                )
            )
        )
    }

    @Test
    @Transactional
    fun should_query_filtered_tag_articles() {
        val tags1 = tagRepository.saveAll(listOf(TagMapper(name = "tag1"), TagMapper(name = "tag2")))
        val tags2 = listOf(tags1.last(), tagRepository.save(TagMapper(name = "tag3")))
        val savedArticleList = articleRepository.saveAll(
            listOf(
                ArticleMapper(
                    title = "title1",
                    description = "description1",
                    body = "body1",
                    tagList = tags1,
                    authorId = "user_id",
                    createdAt = Instant.parse("2016-02-18T03:22:56.637Z"),
                ),
                ArticleMapper(
                    title = "title2",
                    description = "description2",
                    body = "body2",
                    tagList = tags1,
                    authorId = "user_id",
                    createdAt = Instant.parse("2017-02-18T03:22:56.637Z"),
                ),
                ArticleMapper(
                    title = "title3",
                    description = "description3",
                    body = "body3",
                    tagList = tags2,
                    authorId = "user_id",
                    createdAt = Instant.parse("2018-02-18T03:22:56.637Z"),
                ),
                ArticleMapper(
                    title = "title4",
                    description = "description4",
                    body = "body4",
                    tagList = tags2,
                    authorId = "user_id",
                    createdAt = Instant.parse("2019-02-18T03:22:56.637Z"),
                )
            )
        )

        val page1 = articleRepository.findByTag("tag1", PageRequest.of(0, 3, Sort.by("createdAt").descending()))

        assertThat(page1.totalElements, equalTo(2))
        assertThat(
            page1.toList().map { it.id }, equalTo(
                listOf(
                    savedArticleList[1].id,
                    savedArticleList[0].id,
                )
            )
        )

        val page2 = articleRepository.findByTag("tag2", PageRequest.of(0, 3, Sort.by("createdAt").descending()))
        assertThat(page2.totalElements, equalTo(4))
        assertThat(
            page2.toList().map { it.id }, equalTo(
                listOf(
                    savedArticleList[3].id,
                    savedArticleList[2].id,
                    savedArticleList[1].id,
                )
            )
        )
    }
}