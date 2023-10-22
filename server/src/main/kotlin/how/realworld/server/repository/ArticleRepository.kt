package how.realworld.server.repository

import how.realworld.server.model.ArticleId
import how.realworld.server.repository.mapper.ArticleMapper
import how.realworld.server.repository.mapper.TagMapper
import jakarta.persistence.criteria.Predicate
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository

@Repository
interface ArticleRepository: JpaRepository<ArticleMapper, ArticleId>, JpaSpecificationExecutor<ArticleMapper>

fun ArticleRepository.queryList(tagName: String? = null, authorId: String? = null, pageable: Pageable): Page<ArticleMapper> {
    val specification = Specification<ArticleMapper> { root, _, criteriaBuilder ->
        val predicates = mutableListOf<Predicate>()

        if (tagName != null) {
            val join = root.join<ArticleMapper, TagMapper>("tagList")
            predicates.add(criteriaBuilder.equal(join.get<String>("name"), tagName))
        }

        if (authorId != null) {
            predicates.add(criteriaBuilder.equal(root.get<String>("authorId"), authorId))
        }

        criteriaBuilder.and(*predicates.toTypedArray())
    }

    return findAll(specification, pageable)
}