package how.realworld.server.repository

import how.realworld.server.repository.mapper.TagId
import how.realworld.server.repository.mapper.TagMapper
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface TagRepository : JpaRepository<TagMapper, TagId> {
    @Query("select t from TagMapper t where t.name in (:names)")
    fun findByNameList(names: List<String>): List<TagMapper>
}

fun TagRepository.saveOrUpdateAll(tagNames: List<String>): List<TagMapper> {
    val savedTags = findByNameList(tagNames)
    val savedTagsMap = savedTags.associateBy { it.name }
    return tagNames.map { tagName ->
        val tag = savedTagsMap[tagName]
        tag ?: save(TagMapper(name = tagName))
    }
}
