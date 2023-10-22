package how.realworld.server.repository

import how.realworld.server.model.ArticleId
import how.realworld.server.repository.mapper.ArticleMapper
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface ArticleRepository: JpaRepository<ArticleMapper, ArticleId> {
    @Query("SELECT a FROM ArticleMapper a JOIN a.tagList t WHERE t.name = :tagName")
    fun findByTag(tagName: String, pageable: Pageable): Page<ArticleMapper>
}
