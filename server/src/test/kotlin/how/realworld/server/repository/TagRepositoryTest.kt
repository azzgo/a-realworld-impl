package how.realworld.server.repository

import how.realworld.server.repository.mapper.TagMapper
import jakarta.transaction.Transactional
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

class TagRepositoryTest: BaseRepositoryTest() {

    @Autowired
    private lateinit var tagRepository: TagRepository

    @Autowired
    private lateinit var testEntityManager: TestEntityManager


    @Test
    @Transactional
    fun should_save_new_tag_with_old_tag() {
        tagRepository.save(TagMapper(
                name = "tag1"
        ))
        testEntityManager.flush()

        tagRepository.saveOrUpdateAll(listOf("tag1", "tag2"))

        assertThat(tagRepository.count(), equalTo(2))
    }

    @Test
    @Transactional
    fun should_return_empty_when_find_by_name_empty_list() {
        assertThat(tagRepository.findByNameList(emptyList()), equalTo(emptyList()))
    }
}