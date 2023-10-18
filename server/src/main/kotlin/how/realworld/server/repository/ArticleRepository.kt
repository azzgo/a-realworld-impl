package how.realworld.server.repository

import how.realworld.server.model.ArticleId
import how.realworld.server.repository.mapper.ArticleMapper
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ArticleRepository: JpaRepository<ArticleMapper, ArticleId> {

}
