package how.realworld.server.repository.mapper

import jakarta.persistence.*

@Table(name = "t_article_tag", uniqueConstraints = [UniqueConstraint(columnNames = ["article_id", "tag_id"])])
open class ArticleTagMapper (
    @OneToOne(targetEntity = ArticleMapper::class)
    open var article: ArticleMapper,

    @OneToOne(targetEntity = TagMapper::class)
    open var tag: TagMapper
)